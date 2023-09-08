package com.net128.test.entitysort

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.persistence.Table
import javax.persistence.EntityManagerFactory
import javax.persistence.metamodel.EntityType
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

@Component
class EntityScanner(private val entityManagerFactory: EntityManagerFactory) {

    fun getOrderedEntityClasses(): List<KClass<*>> {
        val metaModel = entityManagerFactory.metamodel
        val entities = metaModel.entities
        val sameTableMap = mutableMapOf<String, MutableList<KClass<*>>>()
        val graph = buildDependencyGraph(entities, sameTableMap)
        return topologicalSort(graph, sameTableMap)
    }

    private fun buildDependencyGraph(
        entities: Set<EntityType<*>>,
        sameTableMap: MutableMap<String, MutableList<KClass<*>>>
    ): Map<KClass<*>, List<KClass<*>>> {
        val graph = mutableMapOf<KClass<*>, MutableList<KClass<*>>>()
        
        for (entity in entities) {
            val entityKClass = entity.javaType.kotlin
            graph[entityKClass] = mutableListOf()
            
            val tableName = entityKClass.findAnnotation<Table>()?.name
                ?: entityKClass.simpleName?.uppercase()
            
            if (tableName != null) {
                sameTableMap.getOrPut(tableName) { mutableListOf() }.add(entityKClass)
            }
            
            for (attr in entity.attributes) {
                val attrType = attr.javaType.kotlin
                if (graph.containsKey(attrType)) {
                    graph[entityKClass]?.add(attrType)
                }
            }
        }
        
        return graph
    }

    private fun topologicalSort(
        graph: Map<KClass<*>, List<KClass<*>>>,
        sameTableMap: Map<String, List<KClass<*>>>
    ): List<KClass<*>> {
        val result = mutableListOf<KClass<*>>()
        val visited = mutableSetOf<KClass<*>>()
        
        for (node in graph.keys) {
            if (!visited.contains(node)) {
                dfs(node, graph, visited, result, sameTableMap)
            }
        }
        
        return result.reversed()
    }

    private fun dfs(
        node: KClass<*>,
        graph: Map<KClass<*>, List<KClass<*>>>,
        visited: MutableSet<KClass<*>>,
        result: MutableList<KClass<*>>,
        sameTableMap: Map<String, List<KClass<*>>>
    ) {
        visited.add(node)

        graph[node]?.forEach { neighbor ->
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited, result, sameTableMap)
            }
        }

        val tableName = node.findAnnotation<Table>()?.name ?: node.simpleName?.uppercase()

        if (tableName != null) {
            val sameTableEntities = sameTableMap[tableName]?.filter { it != node } ?: emptyList()

            // Sort entities based on whether they have dependencies or not.
            val sortedEntities = sameTableEntities.sortedBy { graph[it]?.size ?: 0 }

            for (entity in sortedEntities) {
                if (!result.contains(entity)) {
                    result.add(entity)
                }
            }
        }

        result.add(node)
    }
}
