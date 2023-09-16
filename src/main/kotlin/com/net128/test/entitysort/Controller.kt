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
) {

	@GetMapping("/db/tables")
	fun dbTables(): List<String> {
		return schemaScanner.getSortedTableNames()
	}

	@GetMapping("/db/schema")
	fun dbSchema(): String? {
		return schemaScanner.getCurrentSchema()
	}

	@GetMapping("/mermaid/erd")
	fun mermaidErd(@RequestParam(defaultValue = "true") markdown: Boolean = true): String {
		if(markdown)
			return "```mermaid\n${mermaidService.generateMermaidERDiagram(schemaScanner.getSortedTableNames())}\n```"
		return mermaidService.generateMermaidERDiagram(schemaScanner.getSortedTableNames())
	}

	@GetMapping("/graphviz/dot")
	fun graphVizDot(): String {
		return graphVizService.generateGraphvizERDiagram(schemaScanner.getSortedTableNames())
	}

	@GetMapping("/graphviz/svg", produces = ["image/svg+xml"])
	fun renderDotToSvg(): String {
		return graphVizService.generateGraphvizERDiagramSvg(schemaScanner.getSortedTableNames())
	}

	@GetMapping("/dbml/code")
	fun dbMlCode(): String {
		return dbDiagramService.generateDbDiagramERDiagram(schemaScanner.getSortedTableNames())
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