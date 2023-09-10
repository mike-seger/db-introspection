package com.net128.test.entitysort;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

@RestController
class Controller(private val entityScanner: EntityScanner, private val schemaSpyService: SchemaSpyService) {

	@GetMapping
	fun entities(): List<String> {
		return entityScanner.getOrderedEntityClasses().map { it.simpleName }.filterNotNull().toList()
	}

	@GetMapping("/svg/entityStructure")
	fun generateEntityStructureSvg(
		@RequestParam("width", required = false, defaultValue = "800") width: Int,
		@RequestParam("height", required = false, defaultValue = "600") height: Int,
		response: HttpServletResponse
	) {
		response.contentType = "image/svg+xml"
		response.characterEncoding = "UTF-8"

		val svgWriter = response.writer
		svgWriter.write(entityScanner.generateERM())
		svgWriter.flush()
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