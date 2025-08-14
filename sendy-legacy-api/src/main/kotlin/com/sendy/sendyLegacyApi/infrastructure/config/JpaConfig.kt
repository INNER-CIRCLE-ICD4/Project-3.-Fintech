package com.sendy.sendyLegacyApi.infrastructure.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

/**
 *
 * spring configuration
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["com.sendy.sendyLegacyApi"])
class JpaConfig
