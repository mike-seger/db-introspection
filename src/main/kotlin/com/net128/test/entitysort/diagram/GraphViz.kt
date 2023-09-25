package com.net128.test.entitysort.diagram

import com.net128.test.entitysort.data.dbml.DbmlTable
import com.net128.test.entitysort.util.TextUtil.indent
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.engine.GraphvizJdkEngine
import guru.nidi.graphviz.parse.Parser
import org.springframework.stereotype.Service

@Service
@Suppress("SpellCheckingInspection")
class GraphViz {
    @Suppress("unused")
    enum class RankDir { TB, LR, RL, BT }
    fun diagramDefinition(tables: List<DbmlTable>, rankDir: RankDir = RankDir.TB,
            splines: Boolean = true, withDataTypes: Boolean = true): String {
        val sb = StringBuilder()
        val colSpan = if(withDataTypes) 2 else 1

        sb.append("digraph ERD {\n")
        sb.append("\tgraph[rankdir=$rankDir, splines=$splines];\n")
        sb.append("\tnode [shape=record, fontsize=10, fontname=\"Verdana\"];\n")
        sb.append("\tedge [style=solid, color=\"#888888\"];\n")

        tables.forEach { table ->
            val tableName = table.name

            sb.append("\t\"$tableName\" [shape=none, margin=0, label=<")
            sb.append("<table border=\"0\" cellborder=\"1\" cellspacing=\"0\" cellpadding=\"2\">\n")
            sb.append("\t\t\t" +
                "<tr><td height=\"21\" valign=\"bottom\" colspan=\"$colSpan\" bgcolor=\"#1d71b8\" color=\"#aaaaaa\" fontname=\"Helvetica, sans-serif\">" +
                "<font color=\"white\"><b>" +
                tableName.uppercase() +
                "</b></font></td></tr>\n")

            table.columns.forEach { column ->
                val columnName = column.name
                val dataType = column.type.replace(" ", "_")
                val columnCell = if(column.isPrimaryKey == true) "<b>$columnName</b>" else columnName
                val cellStyle = if(column.isPrimaryKey == true) " valign=\"bottom\" height=\"20\"" else ""

                sb.append("<tr>")
                sb.append("<td $cellStyle bgcolor=\"#e8e8e8\" color=\"#aaaaaa\">$columnCell</td>")
                if(withDataTypes) sb.append("<td bgcolor=\"#e8e8e8\"  color=\"#aaaaaa\" align=\"right\">$dataType</td>")
                sb.append("</tr>\n")
            }

            sb.append("</table>>];\n")
        }

        val alreadyProcessedRelations = mutableSetOf<String>()

        tables.forEach { table ->
            table.references.forEach { reference ->
                val pkTableName = reference.toTable
                val fkTableName = table.name // the current table being processed

                // Get the appropriate arrow direction/style based on the reference type
//                val arrowType = when (reference.refType) {
//                    RefType.OneToOne -> " -> "       // a simple directed edge for one-to-one
//                    RefType.OneToMany -> " -> "      // a simple directed edge for one-to-many
//                    RefType.ManyToOne -> " <- "      // you can choose a different style if needed
//                    RefType.ManyToMany -> " <-> "    // a bidirectional edge for many-to-many
//                }

                val arrowType = " -> "

                val relation = "$pkTableName$arrowType$fkTableName"

                if (!alreadyProcessedRelations.contains(relation)) {
                    sb.append(indent(1, "\"$pkTableName\"$arrowType\"$fkTableName\";\n"))
                    alreadyProcessedRelations.add(relation)
                }
            }
        }


        sb.append("}")
        return sb.toString()
    }

    fun svg(tables: List<DbmlTable>): String {
        val dot = diagramDefinition(tables)
        Graphviz.useEngine(GraphvizJdkEngine())

        val graph = Parser().read(dot)
        return Graphviz.fromGraph(graph).render(Format.SVG).toString()
    }
}