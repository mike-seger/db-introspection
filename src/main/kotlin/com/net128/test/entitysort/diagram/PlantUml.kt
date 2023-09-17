package com.net128.test.entitysort.diagram

import com.net128.test.entitysort.data.DbmlTable
import com.net128.test.entitysort.util.TestUtil.indent
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader
import org.springframework.stereotype.Service
import java.io.ByteArrayOutputStream

@Service
class PlantUml {

    fun diagramDefinition(tables: List<DbmlTable>): String {
        val diagramBuilder = StringBuilder("@startuml\n")

        tables.forEach { table ->
            diagramBuilder.append(indent(1, "entity \"${table.name}\" {\n"))
            table.columns.forEach { column ->
                if (column.isPrimaryKey == true) {
                    diagramBuilder.append(indent(2, "+ ${column.name} : ${column.type}\n"))
                } else {
                    diagramBuilder.append(indent(2, "${column.name} : ${column.type}\n"))
                }
            }
            diagramBuilder.append(indent(1, "}\n"))
        }

        tables.forEach { table ->
            table.references.forEach { reference ->
                // Here's a simple relation representation: the table having the FK will have an arrow pointing to the primary table
                diagramBuilder.append(indent(1, "\"${reference.fromTable}\" --> \"${reference.toTable}\"\n"))
            }
        }

        diagramBuilder.append("@enduml")

        return diagramBuilder.toString()
    }

    fun svg(tables: List<DbmlTable>): String {
        val plantUMLSource = diagramDefinition(tables)
        val reader = SourceStringReader(plantUMLSource)
        ByteArrayOutputStream().use { os ->
            reader.outputImage(os, FileFormatOption(FileFormat.SVG))
            return os.toString(Charsets.UTF_8.name())
        }
    }
}
