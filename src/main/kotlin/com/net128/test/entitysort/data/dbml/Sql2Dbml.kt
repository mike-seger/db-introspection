package com.net128.test.entitysort.data.dbml

import com.net128.test.entitysort.data.dbml.Dbml.mapType
import net.sf.jsqlparser.JSQLParserException
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.create.table.CreateTable
import net.sf.jsqlparser.statement.create.table.ForeignKeyIndex

object Sql2Dbml {
 /*   fun parseSql(sql: String): List<DbmlTable> {
        val tables = mutableListOf<DbmlTable>()

        try {
            val statements = CCJSqlParserUtil.parseStatements(sql).statements
            for (stmt in statements) {
                if (stmt is CreateTable) {
                    val tableName = stmt.table.name.lowercase()
                    val columns = stmt.columnDefinitions.map {
                        val typeName = mapType(it.colDataType.dataType).lowercase()
                        val typeArguments = it.colDataType.argumentsStringList
                        val typeWithPrecision = if (typeArguments != null && typeArguments.isNotEmpty()) {
                            "$typeName(${typeArguments.joinToString(", ")})"
                        } else {
                            typeName
                        }
                        val isPrimaryKey = it.columnSpecs?.containsAll(listOf("PRIMARY", "KEY")) ?: false
                        DbmlColumn(it.columnName.lowercase(), typeWithPrecision, isPrimaryKey)
                    }
                    val references = mutableListOf<DbmlReference>()

                    // Iterate over the constraints of the table
                    stmt.indexes?.forEach { index ->
                        if (index is ForeignKeyIndex) {
                            val fromColumns = index.columnsNames.map { it.lowercase() }
                            val toTable = index.table.name.lowercase()
                            val toColumns = index.referencedColumnNames.map { it.lowercase() }

                            fromColumns.zip(toColumns).forEach { (fromColumn, toColumn) ->
                                references.add(DbmlReference(tableName, fromColumn, toTable, toColumn, RefType.ManyToOne))
                            }
                        }
                    }

                    tables.add(DbmlTable(tableName, columns, references))
                }
            }
        } catch (e: JSQLParserException) {
            e.printStackTrace()
        }
        return tables
    }*/

    fun parseSql(sql: String): List<DbmlTable> {
        val tables = mutableListOf<DbmlTable>()

        try {
            val statements = CCJSqlParserUtil.parseStatements(sql).statements
            for (stmt in statements) {
                if (stmt is CreateTable) {
                    val tableName = stmt.table.name.lowercase()
                    val columns = mutableListOf<DbmlColumn>()
                    val references = mutableListOf<DbmlReference>()
                    val primaryKeys = mutableSetOf<String>()

                    stmt.indexes?.forEach { index ->
                        if (index is ForeignKeyIndex) {
                            val fromColumns = index.columnsNames.map { it.lowercase() }
                            val toTable = index.table.name.lowercase()
                            val toColumns = index.referencedColumnNames.map { it.lowercase() }

                            fromColumns.zip(toColumns).forEach { (fromColumn, toColumn) ->
                                references.add(DbmlReference(tableName, fromColumn, toTable, toColumn, RefType.ManyToOne))
                            }
                        } else if (index.type == "PRIMARY KEY") {
                            primaryKeys.addAll(index.columnsNames.map { it.lowercase() })
                        }
                    }

                    stmt.columnDefinitions.forEach {
                        val typeName = mapType(it.colDataType.dataType).lowercase()
                        val typeArguments = it.colDataType.argumentsStringList
                        val typeWithPrecision = if (typeArguments != null && typeArguments.isNotEmpty()) {
                            "$typeName(${typeArguments.joinToString(", ")})"
                        } else {
                            typeName
                        }
                        DbmlColumn(it.columnName.lowercase(), typeWithPrecision, it.columnSpecs?.containsAll(listOf("PRIMARY", "KEY")) ?: false)

                        val isPrimaryKey = it.columnName.lowercase() in primaryKeys || it.columnSpecs?.containsAll(listOf("PRIMARY", "KEY")) == true
                        columns.add(DbmlColumn(it.columnName.lowercase(), typeWithPrecision.lowercase(), isPrimaryKey))
                    }

                    tables.add(DbmlTable(tableName, columns, references.sortedBy { it.toString() }))
                }
            }
        } catch (e: JSQLParserException) {
            e.printStackTrace()
        }
        return tables
    }
}
