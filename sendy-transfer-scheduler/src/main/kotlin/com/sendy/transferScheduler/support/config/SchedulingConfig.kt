package com.sendy.transferScheduler.support.config

import com.sendy.sharedKafka.support.config.SharedKafkaConfig
import com.sendy.transferDomain.support.config.TransferDomainJpaConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration(proxyBeanMethods = false)
@EnableScheduling
@Import(TransferDomainJpaConfig::class, SharedKafkaConfig::class)
@ComponentScan(value = ["com.sendy.transferDomain"])
class SchedulingConfig
