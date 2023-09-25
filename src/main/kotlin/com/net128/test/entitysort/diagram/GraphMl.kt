package com.net128.test.entitysort.diagram

import com.net128.test.entitysort.data.dbml.DbmlTable
import org.springframework.stereotype.Service

@Service
class GraphMl {

    @Suppress("SpellCheckingInspection")
    fun diagramDefinition(tables: List<DbmlTable>): String {
        val graphMLBuilder = StringBuilder()

        graphMLBuilder.append("""
            <?xml version="1.0" encoding="UTF-8"?>
            <graphml xmlns="http://graphml.graphdrawing.org/xmlns"  
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd">
              <graph id="G" edgedefault="directed">
        """.trimIndent())

        // Add nodes
        tables.forEach { table ->
            graphMLBuilder.append("""
                <node id="${table.name}">
                    <data key="label">${table.name}</data>
                </node>
            """.trimIndent())
        }

        // Add edges
        tables.forEach { table ->
            table.references.forEach { reference ->
                graphMLBuilder.append("""
                    <edge source="${reference.fromTable}" target="${reference.toTable}">
                        <data key="label"></data>
                    </edge>
                """.trimIndent())
            }
        }

        graphMLBuilder.append("""
              </graph>
            </graphml>
        """.trimIndent())

        return graphMLBuilder.toString()
    }
}
