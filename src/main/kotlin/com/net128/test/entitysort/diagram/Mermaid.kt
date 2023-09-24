package com.net128.test.entitysort.diagram

import com.net128.test.entitysort.data.dbml.DbmlTable
import com.net128.test.entitysort.data.dbml.RefType
import com.net128.test.entitysort.util.TextUtil.indent
import org.springframework.stereotype.Service

@Service
class Mermaid {

    fun diagramDefinition(tables: List<DbmlTable>): String {
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
                val relationSymbol = when (reference.refType) {
                    RefType.OneToOne -> "||--||"
                    RefType.OneToMany -> "||--o{"
                    RefType.ManyToOne -> "o{--||"
                    RefType.ManyToMany -> "o{--o{"
                }
                diagramBuilder.append(indent(1, "${reference.fromTable} $relationSymbol ${reference.toTable} : \"\"\n"))
            }
        }

        return diagramBuilder.toString()
    }
}