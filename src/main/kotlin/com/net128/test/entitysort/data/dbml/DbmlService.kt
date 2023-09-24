package com.net128.test.entitysort.data

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.stereotype.Service
import java.sql.DatabaseMetaData
import javax.sql.DataSource

@Service
class DbmlService(private val dataSource: DataSource) {

    fun generateDbml(qualifiedTableNames: List<String>? = null, schema: String? = null): List<DbmlTable> {
        dataSource.connection.use { connection ->
            val metaData: DatabaseMetaData = connection.metaData
            val tables = mutableListOf<DbmlTable>()

            // Check if database treats mixed case unquoted SQL identifiers as case-sensitive
            val isCaseSensitive = metaData.storesMixedCaseIdentifiers()

            val effectiveSchema = schema ?: connection.schema

            // If a list of tables is not provided, get all tables from the schema
            val tableNamesToProcess = qualifiedTableNames ?: let {
                val rs = metaData.getTables(null, effectiveSchema, "%", null)
                val tableList = mutableListOf<String>()
                while (rs.next()) {
                    tableList.add(rs.getString("TABLE_NAME"))
                }
                tableList
            }

            tableNamesToProcess.forEach { qualifiedTableName ->
                val parts = qualifiedTableName.split('.')
                val tableName = parts.last()

                val primaryKeys = mutableSetOf<String>()
                metaData.getPrimaryKeys(null, effectiveSchema, tableName).use { pkResultSet ->
                    while (pkResultSet.next()) {
                        val rawColumnName = pkResultSet.getString("COLUMN_NAME")
                        val columnName = if (!isCaseSensitive) rawColumnName.lowercase() else rawColumnName
                        primaryKeys.add(columnName)
                    }
                }

                val columns = mutableListOf<DbmlColumn>()
                val references = mutableListOf<DbmlReference>()

                val columnsRs = metaData.getColumns(null, effectiveSchema, tableName, null)
                while (columnsRs.next()) {
                    val rawColumnName = columnsRs.getString("COLUMN_NAME")
                    val columnName = if (!isCaseSensitive) rawColumnName.lowercase() else rawColumnName
                    var dataType = columnsRs.getString("TYPE_NAME")
                        .replace(" ", "_")
                        .replace("CHARACTER_VARYING", "varchar")
                        .lowercase()

                    val size = columnsRs.getInt("COLUMN_SIZE")
                    val decimalDigits = columnsRs.getInt("DECIMAL_DIGITS")
                    dataType = when {
                        decimalDigits > 0 && dataType == "number" -> "numeric($size, $decimalDigits)"
                        decimalDigits > 0 && dataType == "double_precision" -> "numeric($size, $decimalDigits)"
                        size > 0 && dataType == "double_precision" -> "numeric($size)"
                        dataType == "double_precision" -> "double"
                        size > 0 && (dataType == "varchar" || dataType == "char" || dataType == "number") -> "$dataType($size)"
                        else -> dataType
                    }

                    val isPrimaryKey = primaryKeys.contains(columnName)

                    columns.add(DbmlColumn(columnName, dataType, if(isPrimaryKey) true else null))
                }

                val foreignKeysRs = metaData.getImportedKeys(null, effectiveSchema, tableName)
                while (foreignKeysRs.next()) {
                    val rawPkTableName = foreignKeysRs.getString("PKTABLE_NAME")
                    val pkTableName = if (!isCaseSensitive) rawPkTableName.lowercase() else rawPkTableName
                    val rawPkColumnName = foreignKeysRs.getString("PKCOLUMN_NAME")
                    val pkColumnName = if (!isCaseSensitive) rawPkColumnName.lowercase() else rawPkColumnName

                    val rawFkTableName = foreignKeysRs.getString("FKTABLE_NAME")
                    val fkTableName = if (!isCaseSensitive) rawFkTableName.lowercase() else rawFkTableName
                    val rawFkColumnName = foreignKeysRs.getString("FKCOLUMN_NAME")
                    val fkColumnName = if (!isCaseSensitive) rawFkColumnName.lowercase() else rawFkColumnName

                    val fkUnique = isColumnUnique(metaData, effectiveSchema, fkTableName, fkColumnName)
                    val pkUnique = primaryKeys.contains(pkColumnName)

                    val refType = when {
                        fkUnique && pkUnique -> RefType.OneToOne
                        !fkUnique && pkUnique -> RefType.OneToMany
                        fkUnique && !pkUnique -> RefType.ManyToOne
                        else -> RefType.ManyToMany
                    }

                    references.add(DbmlReference(fkTableName, fkColumnName, pkTableName, pkColumnName, refType))
                }

                val finalTableName = if (!isCaseSensitive) tableName.lowercase() else tableName
                tables.add(DbmlTable(finalTableName, columns, references))
            }

            return tables
        }
    }

    private fun isColumnUnique(metaData: DatabaseMetaData, schema: String, tableName: String, columnName: String): Boolean {
        metaData.getIndexInfo(null, schema, tableName, true, false).use { rs ->
            while (rs.next()) {
                val indexColumnName = rs.getString("COLUMN_NAME")
                val nonUnique = rs.getBoolean("NON_UNIQUE")

                if (indexColumnName == columnName && !nonUnique) {
                    return true
                }
            }
        }
        return false
    }

    fun modelToDbml(tables: List<DbmlTable>): String {
        val sb = StringBuilder()

        tables.forEach { table ->
            sb.append("Table ${table.name} {\n")
            table.columns.forEach { column ->
                sb.append("  ${column.name} ${column.type}")
                if(column.isPrimaryKey == true) sb.append(" [pk]")
                sb.append("\n")
            }

            sb.append("}\n\n")

            table.references.forEach { reference ->
                sb.append("Ref: ${table.name}.${reference.fromColumn} ${reference.refType} ${reference.toTable}.${reference.toColumn}\n")
            }

            if(table.references.isNotEmpty()) sb.append("\n")
        }

        return sb.toString()
    }

    fun parseDbmlToModel(dbml: String): List<DbmlTable> {
        val tables = mutableListOf<DbmlTable>()

        // Split the input by 'Table' to get individual table definitions
        val tableDefinitions = dbml.split(Regex("""\bTable\b""")).drop(1)  // Drop the first split as it'll be empty

        for (tableDefinition in tableDefinitions) {
            val lines = tableDefinition.trim().split("\n")

            val tableName = lines[0].trim().removeSurrounding("`", "`")
            val columns = mutableListOf<DbmlColumn>()
            val references = mutableListOf<DbmlReference>()

            for (i in 1 until lines.size) {
                val line = lines[i].trim()
                when {
                    line.startsWith("`") -> {
                        val parts = line.split(" ")
                        val columnName = parts[0].removeSurrounding("`", "`")
                        val columnType = parts[1]
                        val isPrimaryKEy = "[pk]" == parts[2] || "[primary key]" == parts[2]
                        columns.add(DbmlColumn(columnName, columnType, isPrimaryKEy))
                    }
                    line.startsWith("ref:") -> {
                        // Split based on relationship types, assuming they don't appear in column names
                        val parts = line.split(Regex("""[\[\]<>\- ]+"""))

                        val fromColumn = parts[1]
                        val refType = when {
                            line.contains("<>") -> RefType.ManyToMany
                            line.contains("-") -> RefType.OneToOne
                            line.contains(">") -> RefType.ManyToOne
                            line.contains("<") -> RefType.OneToMany
                            else -> throw IllegalArgumentException("Unknown reference type in line: $line")
                        }

                        val toTable = parts[2]
                        val toColumn = parts[3]

                        references.add(DbmlReference(tableName, fromColumn, toTable, toColumn, refType))
                    }

                }
            }

            tables.add(DbmlTable(tableName, columns, references))
        }

        return tables
    }
}

enum class RefType(private val symbol: String) {
    OneToOne("-"),
    OneToMany("<"),
    ManyToOne(">"),
    ManyToMany("<>");

    override fun toString(): String { return symbol }
}

data class DbmlReference(
    val fromTable: String,
    val fromColumn: String,
    val toTable: String,
    val toColumn: String,
    val refType: RefType
)

data class DbmlTable(
    val name: String,
    val columns: List<DbmlColumn>,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val references: List<DbmlReference> = listOf()
)

data class DbmlColumn(
    val name: String,
    val type: String,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val isPrimaryKey: Boolean? = null
)
