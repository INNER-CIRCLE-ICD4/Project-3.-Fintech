package com.sendy.sharedKafka.support.config

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "spring.kafka")
class SharedKafkaProperties
    @ConstructorBinding
    constructor(
        val bootstrapServers: String?,
        @Value("\${consumer}")
        val consumer: Map<String, String>,
    ) {
        @PostConstruct
        fun init() {
            if (consumer["group-id"].isNullOrEmpty()) {
                throw RuntimeException("kafka spring.kafka.consumer.group-id is must be exist, but group-id property is null or empty")
            }
        }

        val groupId: String = consumer.toProperties().getProperty("group-id")

        override fun toString(): String =
            """
            bootstrapServer: $bootstrapServers
            consumer: $groupId
            """.trimIndent()
    }
