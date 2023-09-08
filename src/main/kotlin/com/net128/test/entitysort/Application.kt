package com.net128.test.entitysort

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EntityScan("com.net128.test.entitysort.model") // Replace with your package name
@EnableJpaRepositories("com.net128.test.entitysort.repository") // Replace with your repository package
@ComponentScan("com.net128.test.entitysort")
class EntitysortApplication

fun main(args: Array<String>) {
	runApplication<EntitysortApplication>(*args)
}
