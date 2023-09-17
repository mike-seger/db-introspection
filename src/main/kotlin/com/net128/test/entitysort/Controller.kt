package com.net128.test.entitysort

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class Controller(
	private val entityScanner: EntityScanner,
	private val schemaScanner: SchemaScanner,
	private val mermaidService: MermaidService,
	private val dbDiagramService: DbDiagramService,
	private val graphVizService: GraphVizService,
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
		val code = mermaidService.generateMermaidERDiagram2(dbmlService.generateDbml())
		if(markdown) return "```mermaid\n$code\n```"
		return code
	}

	@GetMapping("/graphviz/dot", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun graphVizDot(): String {
		return graphVizService.generateGraphvizERDiagram(schemaScanner.getSortedTableNames())
	}

	@GetMapping("/graphviz/svg", produces = ["image/svg+xml"])
	fun renderDotToSvg(): String {
		return graphVizService.generateGraphvizERDiagramSvg(schemaScanner.getSortedTableNames())
	}

	@GetMapping("/dbml/code", produces = [MediaType.TEXT_PLAIN_VALUE])
	fun dbMlCode(): String {
		return dbDiagramService.generateDbDiagramERDiagram(schemaScanner.getSortedTableNames())
	}

	@GetMapping("/dbml/model")
	fun dbml(): List<DbmlTable> {
		return dbmlService.generateDbml(schema = "PUBLIC")
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