package com.sendy.sharedKafka.support.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SharedKafkaProperties(
    @Value("\${spring.kafka.bootstrap-servers:kafka:9092}")
    val bootstrapServers: String,
    
    @Value("\${spring.kafka.consumer.group-id:default-group}")
    val groupId: String
) {
    @PostConstruct
    fun init() {
        if (groupId.isBlank()) {
            throw RuntimeException("kafka spring.kafka.consumer.group-id is must be exist, but group-id property is null or empty")
        }
    }
}
