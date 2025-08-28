package com.sendy.sharedKafka.infrastructure.config

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.support.serializer.JsonDeserializer

@Configuration
@Import(value = [SharedKafkaConfig::class])
class SharedKafkaConsumerConfig(
    private val properties: SharedKafkaProperties,
) {
    val consumerConfigProps =
        mapOf(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to properties.bootstrapServers,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to JsonDeserializer::class.java,
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest", // FIFO, latest: FILO
            ConsumerConfig.GROUP_ID_CONFIG to properties.groupId,
        )

    @Bean
    fun consumerFactory(): ConsumerFactory<String, Any> = DefaultKafkaConsumerFactory(consumerConfigProps)

    @Bean
    fun kafkaListenerContainerFactory(consumerFactory: ConsumerFactory<String, Any>) =
        ConcurrentKafkaListenerContainerFactory<String, Any>().also {
            it.consumerFactory = consumerFactory
            it.containerProperties.ackMode = ContainerProperties.AckMode.RECORD
        }
}
