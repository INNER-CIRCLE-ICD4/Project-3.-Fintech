package com.sendy.transferProducer.infrastructure.config

import com.sendy.sharedKafka.infrastructure.config.SharedKafkaProducerConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration(proxyBeanMethods = false)
@Import(SharedKafkaProducerConfig::class)
@ComponentScan(value = ["com.sendy.sharedKafka"])
class ProducerConfig
