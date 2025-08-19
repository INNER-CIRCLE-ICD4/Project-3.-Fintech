package com.sendy.transferDomain.support.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.JdbcClient
import javax.sql.DataSource

@Configuration(proxyBeanMethods = false)
class TransferDomainJdbcConfig {
    @Bean
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        val jdbcTemplate = JdbcTemplate(dataSource)

        jdbcTemplate.fetchSize = 1_000
        jdbcTemplate.maxRows = 1_000

        return jdbcTemplate
    }

    @Bean
    fun jdbcClient(jdbcTemplate: JdbcTemplate): JdbcClient = JdbcClient.create(jdbcTemplate)
}
