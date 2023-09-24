package com.net128.test.entitysort.data.dbml

import com.fasterxml.jackson.annotation.JsonInclude

object Dbml {
    private val typeMap = mapOf(
        "CHARACTER VARYING" to "VARCHAR",
        "DOUBLE PRECISION" to "DOUBLE"
    )

    fun dbmlTablesToString1(tables: List<DbmlTable>): String {
        val sb = StringBuilder()

        tables.forEach { table ->
            sb.append("Table ${table.name.lowercase()} {\n")

            table.columns.forEach { column ->
                sb.append("  ${column.name} ${mapType(column.type)}".lowercase())
                if(column.isPrimaryKey == true) sb.append(" [pk]")
                sb.append("\n")
            }

            sb.append("}\n\n")

            table.references.forEach { reference ->
                sb.append("Ref: ${table.name.lowercase()}.${reference.fromColumn.lowercase()} ${reference.refType} ${reference.toTable.lowercase()}.${reference.toColumn.lowercase()}\n")
            }

            if(table.references.isNotEmpty()) sb.append("\n")
        }

        return sb.toString()
    }

    fun dbmlTablesToString(tables: List<DbmlTable>): String {
        val builder = StringBuilder()
        for (table in tables) {
            builder.append("Table ${table.name} {\n")
            for (column in table.columns) {
                column.isPrimaryKey
                builder.append("  ${column.name} ${column.type.lowercase()}${
                    if (column.isPrimaryKey == true) " [pk]" else ""}\n")
            }
            builder.append("}\n")
            for (reference in table.references) {
                builder.append("Ref: ${table.name}.${reference.fromColumn} ${reference.refType} ${reference.toTable}.${reference.toColumn}\n")
            }
            if(table.references.isNotEmpty()) builder.append("\n")
        }



        return builder.toString()
    }


    fun parseDbmlString(dbml: String): List<DbmlTable> {
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
                        val columnType = mapType(parts[1])
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

    fun mapType(type: String) : String {
        return if(typeMap[type]!=null) typeMap[type]!! else type
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

