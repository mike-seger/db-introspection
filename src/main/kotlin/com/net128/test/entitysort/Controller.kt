package com.net128.test.entitysort;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class Controller(private val entityScanner: EntityScanner, private val schemaSpyService: SchemaSpyService, val schemaScanner: SchemaScanner) {

	@GetMapping
	fun entities(): List<String> {
		return entityScanner.getOrderedEntityClasses().map { it.simpleName }.filterNotNull().toList()
	}

	@GetMapping("/db/tables")
	fun dbTables(): List<String> {
		return schemaScanner.getSortedTableNames()
	}

	@GetMapping("/db/schema")
	fun dbSchema(): String? {
		return schemaScanner.getCurrentSchema()
	}

	@GetMapping("/db/erd")
	fun dbErd(): String {
		return schemaScanner.generateMermaidERDiagram(schemaScanner.getSortedTableNames())
	}


	@GetMapping("/entityStructure")
	fun generateEntityStructure() : Map<String, Any> {
		return entityScanner.getEntityStructure()
	}

	@GetMapping("/schema")
	fun generateEntityStructureSvg() : String {
		return schemaSpyService.generateSchemaDocumentation().toFile().absolutePath
	}

	@GetMapping("/erm")
	fun erm(): Map<String, Any> {
		return entityScanner.getEntityStructure()
	}
}