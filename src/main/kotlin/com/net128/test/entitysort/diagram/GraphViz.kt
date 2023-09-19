package com.net128.test.entitysort.diagram

import com.net128.test.entitysort.data.DbmlTable
import com.net128.test.entitysort.data.RefType
import com.net128.test.entitysort.util.TestUtil.indent
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.engine.GraphvizJdkEngine
import guru.nidi.graphviz.parse.Parser
import org.springframework.stereotype.Service

@Service
class GraphViz {

    fun diagramDefinition(tables: List<DbmlTable>): String {
        val sb = StringBuilder()

        sb.append("digraph ERD {\n")
        sb.append("\tgraph[rankdir=LR, splines=true];\n")
        sb.append("\tnode [shape=record, fontsize=10, fontname=\"Verdana\"];\n")
        sb.append("\tedge [style=solid, color=\"gray\"];\n")

        tables.forEach { table ->
            val tableName = table.name

            sb.append("\t\"$tableName\" [shape=none, margin=0, label=<")
            sb.append("<table border=\"0\" cellborder=\"1\" cellspacing=\"0\" cellpadding=\"2\">\n")
            sb.append("\t\t\t<tr><td bgcolor=\"dodgerblue4\" color=\"white\"><font color=\"white\">$tableName</font></td></tr>\n")

            table.columns.forEach { column ->
                val columnName = column.name
                val dataType = column.type
                    .replace(" ", "_")
                    .replace("CHARACTER_VARYING", "VARCHAR")

                sb.append("<tr><td>$columnName : $dataType\u2001\u2003\u2001\u2001\u2001</td></tr>\n")
            }

            sb.append("</table>>];\n")
        }

        val alreadyProcessedRelations = mutableSetOf<String>()

        tables.forEach { table ->
            table.references.forEach { reference ->
                val pkTableName = reference.toTable
                val fkTableName = table.name // the current table being processed

                // Get the appropriate arrow direction/style based on the reference type
                val arrowType = when (reference.refType) {
                    RefType.OneToOne -> " -> "       // a simple directed edge for one-to-one
                    RefType.OneToMany -> " -> "      // a simple directed edge for one-to-many
                    RefType.ManyToOne -> " <- "      // you can choose a different style if needed
                    RefType.ManyToMany -> " <-> "    // a bidirectional edge for many-to-many
                }

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