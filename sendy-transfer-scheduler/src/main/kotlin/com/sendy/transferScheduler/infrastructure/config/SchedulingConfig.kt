package com.sendy.transferScheduler.infrastructure.config

import com.sendy.transferDomain.infrastructure.config.TransferDomainJpaConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration(proxyBeanMethods = false)
@EnableScheduling
@Import(TransferDomainJpaConfig::class)
@ComponentScan(value = ["com.sendy.transferDomain"])
class SchedulingConfig
