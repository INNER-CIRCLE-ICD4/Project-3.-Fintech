package com.sendy.transferScheduler.infrastructure.config

import com.sendy.sharedKafka.infrastructure.config.SharedKafkaProducerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.config.TopicBuilder

@Configuration(proxyBeanMethods = false)
@Import(SharedKafkaProducerConfig::class)
@ComponentScan(value = ["com.sendy.sharedKafka"])
class SchedulerKafkaConfig {
    @Bean
    fun reservationTopic() = TopicBuilder.name("transfer-scheduler.transfer.reservation.started").partitions(1).replicas(1)
}
