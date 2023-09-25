package com.net128.test.entitysort

import com.net128.test.entitysort.data.*
import com.net128.test.entitysort.data.dbml.Dbml
import com.net128.test.entitysort.data.dbml.DbmlService
import com.net128.test.entitysort.data.dbml.DbmlTable
import com.net128.test.entitysort.diagram.GraphMl
import com.net128.test.entitysort.diagram.GraphViz
import com.net128.test.entitysort.diagram.Mermaid
import com.net128.test.entitysort.diagram.PlantUml
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class Controller(
	private val entityScanner: EntityScanner,
	private val schemaScanner: SchemaScanner,
	private val mermaid: Mermaid,
	private val graphViz: GraphViz,
	private val plantUml: PlantUml,
	private val graphMl: GraphMl,
	private val dbmlService: DbmlService
) {

	@GetMapping("/db/tables")
	fun dbTables(): List<String> {
		return schemaScanner.getSortedTableNames()
	}

	@GetMapping("/db/schema", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun dbSchema(): String? {
		return schemaScanner.getCurrentSchema()
	}

	@GetMapping("/mermaid/erd", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun mermaidErd2(@RequestParam(defaultValue = "true") markdown: Boolean = true): String {
		val code = mermaid.diagramDefinition(dbmlService.extractDbml())
		if(markdown) return "```mermaid\n$code\n```"
		return code
	}

	@GetMapping("/graphviz/dot", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun graphVizDot(): String {
		return graphViz.diagramDefinition(dbmlService.extractDbml())
	}

	@GetMapping("/graphviz/svg", produces = ["image/svg+xml"])
	fun renderDotToSvg(): String {
		return graphViz.svg(dbmlService.extractDbml())
	}

	@GetMapping("/graphml/xml", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun graphMl(): String {
		return graphMl.diagramDefinition(dbmlService.extractDbml())
	}

	@GetMapping("/plantuml/definition", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun plantUmlDefinition(): String {
		return plantUml.diagramDefinition(dbmlService.extractDbml())
	}

	@GetMapping("/plantuml/svg", produces = ["image/svg+xml"])
	fun plantUmlSvg(): String {
		return plantUml.svg(dbmlService.extractDbml())
	}

	@GetMapping("/dbml/code", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun dbMlCode(): String {
		return Dbml.dbmlTablesToString(dbmlService.extractDbml())
	}

	@GetMapping("/dbml/model")
	fun dbml(): List<DbmlTable> {
		return dbmlService.extractDbml(schema = "PUBLIC")
	}

	@GetMapping("/jpa/entities")
	fun jpaEntities(): List<String> {
		return entityScanner.getOrderedEntityClasses().mapNotNull { it.simpleName }.toList()
	}

	@GetMapping("/jpa/structure")
	fun jpaStructure() : Map<String, Any> {
		return entityScanner.getEntityStructure()
	}
}