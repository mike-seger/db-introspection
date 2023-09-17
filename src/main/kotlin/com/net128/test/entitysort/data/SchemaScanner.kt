package com.net128.test.entitysort.data

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

    fun getCurrentSchema(): String? {
        dataSource.connection.use { connection ->
            return connection.schema
        }
    }

}