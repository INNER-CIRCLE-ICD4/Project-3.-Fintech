package com.sendy.sendyLegacyApi.infrastructure.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
@ComponentScan(basePackages = ["com.sendy.sharedKafka"])
class KafkaConfig