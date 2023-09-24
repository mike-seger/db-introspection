package com.net128.test.entitysort.data.dbml

import com.net128.test.entitysort.data.dbml.Dbml.dbmlTablesToString
import com.net128.test.entitysort.data.dbml.Sql2Dbml.parseSql
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class Sql2DbmlTest {
    @ParameterizedTest
    @CsvSource(
        "/sql/complex-db-ddl.sql",
        "/sql/simple-db-ddl.sql"
    )
    fun testParseSql(sqlFile: String) {
        val sql = DbmlTable::class.java.getResourceAsStream(sqlFile)?.bufferedReader().use { it?.readText() }
            ?: throw IllegalArgumentException("Resource not found")

        val dbmlTables = parseSql(sql.trim())

        println(dbmlTablesToString(dbmlTables))
    }

}






