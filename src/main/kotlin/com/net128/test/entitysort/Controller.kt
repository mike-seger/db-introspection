package com.net128.test.entitysort

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
class Controller(private val entityScanner: EntityScanner, val schemaScanner: SchemaScanner) {

	@GetMapping("/db/tables")
	fun dbTables(): List<String> {
		return schemaScanner.getSortedTableNames()
	}

	@GetMapping("/db/schema")
	fun dbSchema(): String? {
		return schemaScanner.getCurrentSchema()
	}

	@GetMapping("/db/erd")
	fun dbErd(@RequestParam markdown: Boolean = true): String {
		if(markdown)
			return "```mermaid\n${schemaScanner.generateMermaidERDiagram(schemaScanner.getSortedTableNames())}\n```"
		return schemaScanner.generateMermaidERDiagram(schemaScanner.getSortedTableNames())
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