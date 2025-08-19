package com.sendy.sharedKafka.support.config

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka

@Configuration
@EnableKafka
@ImportAutoConfiguration(exclude = [KafkaAutoConfiguration::class])
class SharedKafkaConfig
