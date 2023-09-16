package com.net128.test.entitysort

import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class MermaidService(private val dataSource: DataSource) {

    fun generateMermaidERDiagram(tableNames: List<String>): String {
        dataSource.connection.use { connection ->
            val metaData = connection.metaData
            val diagramBuilder = StringBuilder("erDiagram\n")

            tableNames.forEach { tableName ->
                // Split schema and table name
                val parts = tableName.split(".")
                val schemaName = parts[0]
                val table = parts[1]

                // Fetch all columns and identify primary keys
                val primaryKeys = mutableSetOf<String>()
                metaData.getPrimaryKeys(null, schemaName, table).use { pkResultSet ->
                    while (pkResultSet.next()) {
                        primaryKeys.add(pkResultSet.getString("COLUMN_NAME"))
                    }
                }

                // Append all columns
                diagramBuilder.append("    $table {\n")
                metaData.getColumns(null, schemaName, table, null).use { columnResultSet ->
                    while (columnResultSet.next()) {
                        val columnName = columnResultSet.getString("COLUMN_NAME").lowercase()
                        var columnType = columnResultSet.getString("TYPE_NAME")
                        columnType = processDataType(columnType)

                        if (primaryKeys.contains(columnName)) {
                            diagramBuilder.append("        $columnType $columnName PK\n")
                        } else {
                            diagramBuilder.append("        $columnType $columnName\n")
                        }
                    }
                }
                diagramBuilder.append("    }\n")

                // Fetch foreign keys
                metaData.getImportedKeys(null, schemaName, table).use { fkResultSet ->
                    while (fkResultSet.next()) {
                        val fkTableName = fkResultSet.getString("PKTABLE_NAME")
                        val refTableName = fkResultSet.getString("FKTABLE_NAME")
                        diagramBuilder.append("    $fkTableName ||--o{ $refTableName : \"\"\n")
                    }
                }
            }

            return diagramBuilder.toString()
        }
    }

    private fun processDataType(type: String): String {
        val substitutions = mapOf(
            "CHARACTER VARYING" to "VARCHAR",
            "INTEGER" to "INT",
            // Add other substitutions as needed
            // e.g., "NUMERIC" to "DECIMAL"
        )

        val substitutedType = substitutions[type] ?: type
        return substitutedType.replace(" ", "_").lowercase()
    }
}