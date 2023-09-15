package com.net128.test.entitysort

import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class SchemaScanner(private val dataSource: DataSource) {
    fun getSortedTableNames(schema: String? = null, qualified: Boolean = true): List<String> {
        val tableNames = mutableListOf<String>()
        var actualSchema = schema

        dataSource.connection.use { connection ->
            // If the schema is not provided, fetch the current schema from the connection
            if (actualSchema == null) {
                actualSchema = connection.schema
            }

            val resultSet = connection.metaData.getTables(null, actualSchema, "%", arrayOf("TABLE"))
            while (resultSet.next()) {
                val tableName = resultSet.getString("TABLE_NAME")

                // If the qualified flag is true, add the schema name as prefix
                if (qualified) {
                    tableNames.add("$actualSchema.$tableName")
                } else {
                    tableNames.add(tableName)
                }
            }
        }

        return tableNames.sorted()
    }

    fun generateMermaidERDiagram(tableNames: List<String>): String {
        dataSource.connection.use { connection ->
            val metaData = connection.metaData
            val diagramBuilder = StringBuilder("erDiagram\n")

            tableNames.forEach { tableName ->
                // Split schema and table name
                val parts = tableName.split(".")
                val schemaName = parts[0]
                val table = parts[1]

                // Fetch primary key
                metaData.getPrimaryKeys(null, schemaName, table).use { pkResultSet ->
                    while (pkResultSet.next()) {
                        val columnName = pkResultSet.getString("COLUMN_NAME")
                        diagramBuilder.append("    $table {\n        string $columnName PK\n    }\n")
                    }
                }

                // Fetch foreign keys
                metaData.getImportedKeys(null, schemaName, table).use { fkResultSet ->
                    while (fkResultSet.next()) {
                        val fkTableName = fkResultSet.getString("PKTABLE_NAME")
                        val fkColumnName = fkResultSet.getString("PKCOLUMN_NAME")
                        val refTableName = fkResultSet.getString("FKTABLE_NAME")
                        val refColumnName = fkResultSet.getString("FKCOLUMN_NAME")
                        diagramBuilder.append("    $fkTableName ||--o{ $refTableName : references\n")
                    }
                }
            }

            return diagramBuilder.toString()
        }
    }

    fun generateMermaidERDiagram1(tableNames: List<String>): String {
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
                        val columnName = columnResultSet.getString("COLUMN_NAME")
                        val columnType = columnResultSet.getString("TYPE_NAME")
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
                        diagramBuilder.append("    $fkTableName ||--o{ $refTableName : references\n")
                    }
                }
            }

            return diagramBuilder.toString()
        }
    }

    fun getCurrentSchema(): String? {
        dataSource.connection.use { connection ->
            return connection.schema
        }
    }

}