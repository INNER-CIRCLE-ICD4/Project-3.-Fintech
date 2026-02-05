package com.sendy.transferConsumer.infrastructure.config

import com.sendy.sharedKafka.infrastructure.config.SharedKafkaConsumerConfig
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration(proxyBeanMethods = false)
@Import(SharedKafkaConsumerConfig::class)
@ComponentScan(value = ["com.sendy.sharedKafka"])
class ConsumerConfig
