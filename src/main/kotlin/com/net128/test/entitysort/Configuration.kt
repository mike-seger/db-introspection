package com.net128.test.entitysort

import org.h2.tools.Server
import org.springframework.boot.autoconfigure.integration.IntegrationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.sql.SQLException


@Configuration
class Configuration {
    @Bean(initMethod = "start", destroyMethod = "stop")
    fun inMemoryH2Server(): Server {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9090")
    }
}