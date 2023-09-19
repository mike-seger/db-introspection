package com.net128.test.entitysort.diagram

import com.net128.test.entitysort.data.DbmlTable
import com.net128.test.entitysort.data.RefType
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
                val relationSymbol = when (reference.refType) {
                    RefType.OneToOne -> "--"
                    RefType.OneToMany -> "--o"
                    RefType.ManyToOne -> "o--"
                    RefType.ManyToMany -> "o--o"
                }
                diagramBuilder.append(indent(1, "\"${reference.fromTable}\" $relationSymbol \"${reference.toTable}\"\n"))
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
