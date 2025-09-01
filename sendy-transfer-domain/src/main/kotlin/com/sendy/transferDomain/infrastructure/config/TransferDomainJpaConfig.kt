package com.sendy.transferDomain.infrastructure.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = ["com.sendy.transferDomain.infrastructure"])
@EntityScan(basePackages = ["com.sendy.transferDomain.infrastructure"])
class TransferDomainJpaConfig
