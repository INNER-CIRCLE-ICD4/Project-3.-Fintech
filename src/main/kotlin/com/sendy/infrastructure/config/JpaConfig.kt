package com.sendy.infrastructure.config

import jakarta.persistence.EntityManagerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import javax.sql.DataSource

/**
 *
 * spring configuration
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["com.sendy.domain", "com.sendy.infrastructure.persistence"])
class JpaConfig {
    @Bean
    fun entityManagerFactory(datasource: DataSource): LocalContainerEntityManagerFactoryBean {
        val emf = LocalContainerEntityManagerFactoryBean()
        emf.dataSource = datasource

        return emf
    }

    @Bean
    fun platformTransactionManager(emf: EntityManagerFactory): PlatformTransactionManager = JpaTransactionManager(emf)
}
