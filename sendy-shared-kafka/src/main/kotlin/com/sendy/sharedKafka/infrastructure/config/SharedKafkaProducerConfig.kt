package com.sendy.sharedKafka.infrastructure.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.serializer.JsonSerializer

@Configuration
@Import(value = [SharedKafkaConfig::class])
class SharedKafkaProducerConfig(
    private val properties: SharedKafkaProperties,
) {
    val productConfigProps =
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to JsonSerializer::class.java,
            ProducerConfig.ACKS_CONFIG to "all",
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG to true,
            ProducerConfig.RETRIES_CONFIG to 3,
        )

    @Bean
    fun producerFactory(): ProducerFactory<String, Any> = DefaultKafkaProducerFactory(productConfigProps)

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, Any> = KafkaTemplate(producerFactory())
}
