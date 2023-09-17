package com.net128.test.entitysort

import com.net128.test.entitysort.util.TestUtil.indent
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.engine.GraphvizJdkEngine
import guru.nidi.graphviz.parse.Parser
import org.springframework.stereotype.Service
import javax.sql.DataSource

@Service
class GraphVizService(private val dataSource: DataSource) {

    fun generateGraphvizERDiagram(tables: List<DbmlTable>): String {
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
                val relation = "$pkTableName -> $fkTableName"

                if (!alreadyProcessedRelations.contains(relation)) {
                    sb.append(indent(1, "\"$pkTableName\" -> \"$fkTableName\";\n"))
                    alreadyProcessedRelations.add(relation)
                }
            }
        }

        sb.append("}")
        return sb.toString()
    }

    fun generateGraphvizERDiagramSvg(tables: List<DbmlTable>): String {
        val dot = generateGraphvizERDiagram(tables)
        Graphviz.useEngine(GraphvizJdkEngine())

        val graph = Parser().read(dot)
        return Graphviz.fromGraph(graph).render(Format.SVG).toString()
    }
}