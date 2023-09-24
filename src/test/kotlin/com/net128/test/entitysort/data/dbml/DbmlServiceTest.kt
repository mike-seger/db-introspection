package com.net128.test.entitysort.data.dbml

import com.net128.test.entitysort.data.dbml.Dbml.dbmlTablesToString
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class DbmlServiceTest {
    @Autowired
    private lateinit var dbmlService: DbmlService
    @Test
    fun testExtractDbml() {
        println(dbmlTablesToString(dbmlService.extractDbml()))
    }
}