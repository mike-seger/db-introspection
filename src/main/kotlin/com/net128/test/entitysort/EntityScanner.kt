package com.net128.test.entitysort

import org.springframework.stereotype.Component
import javax.persistence.EntityManagerFactory
import javax.persistence.metamodel.EntityType
import kotlin.reflect.KClass

@Component
class EntityScanner(private val entityManagerFactory: EntityManagerFactory) {

    fun getOrderedEntityClasses(): List<KClass<*>> {
        val metaModel = entityManagerFactory.metamodel
        val entities = metaModel.entities
        val graph = buildDependencyGraph(entities)
        return topologicalSort(graph)
    }

    private fun buildDependencyGraph(entities: Set<EntityType<*>>): Map<KClass<*>, List<KClass<*>>> {
        val graph = mutableMapOf<KClass<*>, MutableList<KClass<*>>>()

        for (entity in entities) {
            val entityKClass = entity.javaType.kotlin
            graph[entityKClass] = mutableListOf()

            for (attr in entity.attributes) {
                if (attr.isAssociation) {
                    val attrType = attr.javaType.kotlin
                    graph[entityKClass]?.add(attrType)
                }
            }
        }

        return graph
    }

    private fun topologicalSort(
        graph: Map<KClass<*>, List<KClass<*>>>
    ): List<KClass<*>> {
        val result = mutableListOf<KClass<*>>()
        val visited = mutableSetOf<KClass<*>>()
        val currentPath = mutableSetOf<KClass<*>>()
        // Set of entities that are dependencies for other entities
        val dependentEntities = graph.values.flatten().toSet()

        // Adjusting the noDependencies logic
        val noDependencies = graph.keys.filter {
            graph[it]?.isEmpty() == true && it !in dependentEntities
        }.sortedBy { it.simpleName }

        fun visit(node: KClass<*>) {
            if (!visited.contains(node)) {
                if (currentPath.contains(node)) {
                    throw RuntimeException("Cyclic dependency detected involving ${node.simpleName}.")
                }

                currentPath.add(node)
                for (dependency in graph[node] ?: emptyList()) {
                    visit(dependency)
                }
                currentPath.remove(node)

                visited.add(node)
                if (node !in result) {
                    result.add(node)
                }
            }
        }

        // Only start with entities that have dependencies
        for (entity in graph.keys.filter { it !in noDependencies }) {
            visit(entity)
        }

        // Then, append the entities without dependencies
        result.addAll(noDependencies)

        return result
    }
}
