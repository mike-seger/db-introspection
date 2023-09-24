package com.net128.test.entitysort

import org.h2.tools.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class Configuration {
    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnExpression("'\${h2.server.port}' != '-'")
    fun inMemoryH2Server(@Value("\${h2.server.port:9092}") port: Int): Server {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", port.toString())
    }
}