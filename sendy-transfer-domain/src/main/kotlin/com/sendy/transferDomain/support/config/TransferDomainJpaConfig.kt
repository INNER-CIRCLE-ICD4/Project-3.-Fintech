package com.sendy.transferDomain.support.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaRepositories(basePackages = ["com.sendy.transferDomain.infrastructure"])
@EntityScan(basePackages = ["com.sendy.transferDomain.infrastructure"])
class TransferDomainJpaConfig
