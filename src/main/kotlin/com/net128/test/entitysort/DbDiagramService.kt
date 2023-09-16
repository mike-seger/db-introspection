package com.net128.test.entitysort

import org.springframework.stereotype.Service
import javax.sql.DataSource
import java.sql.Connection
import java.sql.DatabaseMetaData


@Service
class DbDiagramService(private val dataSource: DataSource) {

    fun generateDbDiagramERDiagram(qualifiedTableNames: List<String>): String {
        val connection: Connection = dataSource.connection
        val metaData: DatabaseMetaData = connection.metaData

        val sb = StringBuilder()

        qualifiedTableNames.forEach { qualifiedTableName ->
            val parts = qualifiedTableName.split('.')
            val schemaName = parts[0]
            val tableName = parts[1]

            sb.append("Table $tableName {\n")

            val columnsRs = metaData.getColumns(null, schemaName, tableName, null)
            while (columnsRs.next()) {
                val columnName = columnsRs.getString("COLUMN_NAME")
                val dataType = columnsRs.getString("TYPE_NAME").replace(" ", "_").replace("CHARACTER_VARYING", "VARCHAR")
                val size = columnsRs.getString("COLUMN_SIZE")

                sb.append("  $columnName $dataType")
                if (size != null) sb.append("($size)")
                sb.append("\n")
            }

            sb.append("}\n\n")

            val primaryKeysRs = metaData.getPrimaryKeys(null, schemaName, tableName)
            while (primaryKeysRs.next()) {
                val pkName = primaryKeysRs.getString("PK_NAME")
                val columnName = primaryKeysRs.getString("COLUMN_NAME")
                sb.append("Ref: $tableName.$columnName\n")  // You might need more logic for relations between tables
            }

            sb.append("\n")
        }

        connection.close()
        return sb.toString()
    }
}