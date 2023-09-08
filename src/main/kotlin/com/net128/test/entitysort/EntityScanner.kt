package com.net128.test.entitysort

import org.springframework.stereotype.Component
import javax.persistence.EntityManagerFactory
import javax.persistence.Id
import javax.persistence.metamodel.EntityType
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.full.*

@Component
class EntityScanner(private val entityManagerFactory: EntityManagerFactory) {

    fun getOrderedEntityClasses(): List<KClass<*>> {
        val metaModel = entityManagerFactory.metamodel
        val entities = metaModel.entities
        val graph = buildDependencyGraph(entities)
        return topologicalSort(graph)
    }

    fun getEntityStructure(): Map<String, Any> {
        val orderedEntityClasses = getOrderedEntityClasses()
        val resultStructure = mutableMapOf<String, Any>()

        val nestedEntities = mutableSetOf<KClass<*>>()

        // Identify all nested entities
        orderedEntityClasses.forEach { entity ->
            entity.memberProperties.forEach { prop ->
                if (orderedEntityClasses.contains(prop.returnType.classifier as? KClass<*>)) {
                    nestedEntities.add(prop.returnType.classifier as KClass<*>)
                }
            }
        }

        // Add only the top-level entities to the result
        orderedEntityClasses.forEach { entity ->
            if (entity !in nestedEntities) {
                resultStructure[entity.simpleName ?: "Unknown"] = generateEntityStructure(entity, orderedEntityClasses)
            }
        }

        return resultStructure
    }

    fun generateEntityStructure(entityClass: KClass<*>, orderedEntityClasses: List<KClass<*>>, visited: MutableSet<KClass<*>> = mutableSetOf()): Map<String, Any> {
        if (entityClass in visited) return emptyMap()
        visited.add(entityClass)

        val structure = LinkedHashMap<String, Any>()

        // 1. Find all properties
        val allProperties = entityClass.memberProperties

        // 2. Find the @Id property, if it exists
        val idProperty = allProperties.firstOrNull { it.annotations.any { annotation -> annotation is Id } }

        // 3. If there's an @Id property, add it first
        if (idProperty != null) {
            structure[idProperty.name] = idProperty.returnType.toString()
        }

        // 4. Process other properties
        allProperties.filterNot { it === idProperty }.forEach { prop ->
            val nestedClass = prop.returnType.classifier as? KClass<*>
            if (nestedClass != null && nestedClass in orderedEntityClasses && nestedClass !in visited) {
                structure[prop.name] = generateEntityStructure(nestedClass, orderedEntityClasses, visited)
            } else {
                structure[prop.name] = prop.returnType.toString()
            }
        }

        return structure
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
