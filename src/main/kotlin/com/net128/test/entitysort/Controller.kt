package com.net128.test.entitysort;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val entityScanner: EntityScanner) {

	@GetMapping
	fun entities(): List<String> {
		return entityScanner.getOrderedEntityClasses().map { it.simpleName }.filterNotNull().toList()
	}
}