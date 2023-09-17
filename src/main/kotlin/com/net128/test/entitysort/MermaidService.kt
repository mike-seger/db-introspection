package com.net128.test.entitysort

import com.net128.test.entitysort.util.TestUtil.indent
import org.springframework.stereotype.Service

@Service
class MermaidService {

    fun generateMermaidERDiagram2(tables: List<DbmlTable>): String {
        val diagramBuilder = StringBuilder("erDiagram\n")

        tables.forEach { table ->
            diagramBuilder.append(indent(1, "${table.name} {\n"))

            table.columns.forEach { column ->
                if (column.isPrimaryKey == true) {
                    diagramBuilder.append(indent(2, "${column.name} ${column.type} PK\n"))
                } else {
                    diagramBuilder.append(indent(2, "${column.name} ${column.type}\n"))
                }
            }
            diagramBuilder.append(indent(1, "}\n"))

            table.references.forEach { reference ->
                diagramBuilder.append(indent(1, "${reference.fromTable} ||--o{ ${reference.toTable} : \"\"\n"))
            }
        }

        return diagramBuilder.toString()
    }
}