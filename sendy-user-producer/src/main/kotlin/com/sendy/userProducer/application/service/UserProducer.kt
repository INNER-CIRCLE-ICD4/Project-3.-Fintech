package com.sendy.userProducer.application.service

import com.sendy.sharedKafka.domain.EventMessageRepository
import com.sendy.sharedKafka.domain.EventPublisher
import com.sendy.sharedKafka.topic.KafkaTopics
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserProducer(
    private val eventMessageRepository: EventMessageRepository,
    private val eventPublisher: EventPublisher,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "1s")
    @Transactional
    fun polling() {
        logger.info("user-producer polling start..")
        val userEventMessageList = eventMessageRepository.getUserEventMessage()

        if (userEventMessageList.isNotEmpty()) {
            logger.info("get event message size: {}", userEventMessageList.size)
            userEventMessageList.forEach {
                runCatching {
                    eventMessageRepository.savePublish(it.id)
                }.onSuccess {
                    eventPublisher.publish(
                        KafkaTopics.eventVerfitied,
                        it,
                    )
                }.onFailure {
                    logger.error(it.message)
                }
            }
        }
    }
}
