package com.net128.test.entitysort

import org.springframework.stereotype.Service
import javax.sql.DataSource
import java.sql.Connection
import java.sql.DatabaseMetaData


@Service
class GraphVizService(private val dataSource: DataSource) {

    fun generateGraphvizERDiagram(qualifiedTableNames: List<String>): String {
        val connection: Connection = dataSource.connection
        val metaData: DatabaseMetaData = connection.metaData

        val sb = StringBuilder()

        sb.append("digraph ERD {\n")
        sb.append("\tgraph[rankdir=LR, overlap=false, splines=true];\n")
        sb.append("\tnode [shape=record, fontsize=10, fontname=\"Verdana\"];\n")
        sb.append("\tedge [style=solid, color=\"gray\"];\n")

        qualifiedTableNames.forEach { qualifiedTableName ->
            val parts = qualifiedTableName.split('.')
            val schemaName = parts[0]
            val tableName = parts[1]

            sb.append("\t\"$tableName\" [shape=none, margin=0, label=<")
            sb.append("<table border=\"0\" cellborder=\"1\" cellspacing=\"0\" cellpadding=\"2\">\n")
//            sb.append("\t\t\t<tr><td bgcolor=\"lightblue\">$tableName</td></tr>\n")
            sb.append("\t\t\t<tr><td bgcolor=\"dodgerblue4\" color=\"white\"><font color=\"white\">$tableName</font></td></tr>\n")

            //sb.append("    \"$tableName\" [label=<<TABLE><TR><TD>$tableName</TD></TR>\n")

            val columnsRs = metaData.getColumns(null, schemaName, tableName, null)
            while (columnsRs.next()) {
                val columnName = columnsRs.getString("COLUMN_NAME")
                val dataType = columnsRs.getString("TYPE_NAME")
                    .replace(" ", "_")
                    .replace("CHARACTER_VARYING", "VARCHAR")
                    .replace(" ", "_")

                sb.append("<tr><td>$columnName : $dataType\u2001\u2003</td></tr>\n")
            }

            sb.append("</table>>];\n")
        }

        val alreadyProcessedRelations = mutableSetOf<String>()

        qualifiedTableNames.forEach { qualifiedTableName ->
            val parts = qualifiedTableName.split('.')
            val schemaName = parts[0]
            val tableName = parts[1]

            val foreignKeysRs = metaData.getImportedKeys(null, schemaName, tableName)
            while (foreignKeysRs.next()) {
                val pkTableName = foreignKeysRs.getString("PKTABLE_NAME")
                val fkTableName = foreignKeysRs.getString("FKTABLE_NAME")
                val relation = "$pkTableName -> $fkTableName"

                if (!alreadyProcessedRelations.contains(relation)) {
                    sb.append("    \"$pkTableName\" -> \"$fkTableName\";\n")
                    alreadyProcessedRelations.add(relation)
                }
            }
        }

        sb.append("}")

        connection.close()
        return sb.toString()
    }

}