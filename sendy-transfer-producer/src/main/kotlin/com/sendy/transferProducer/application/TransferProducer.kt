package com.sendy.transferProducer.application

import com.sendy.sharedKafka.domain.EventMessageRepository
import com.sendy.sharedKafka.domain.EventPublisher
import com.sendy.sharedKafka.support.constant.EventTypes
import com.sendy.sharedKafka.topic.KafkaTopics
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class TransferProducer(
    private val eventMessageRepository: EventMessageRepository,
    private val eventPublisher: EventPublisher,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "1s")
    @Transactional
    fun polling() {
        logger.info("transfer producer polling start..")
        val transferEventMessageList = eventMessageRepository.getTransferEventMessage()

        if (transferEventMessageList.isNotEmpty()) {
            transferEventMessageList.forEach {
                when (it.type) {
                    EventTypes.TRANSFER_SUCCEED -> {
                        eventMessageRepository.savePublish(it.id)
                        eventPublisher.publish(KafkaTopics.eventTransferSucceed, it)
                    }

                    EventTypes.TRANSFER_FAILED -> {
                        eventMessageRepository.savePublish(it.id)
                        eventPublisher.publish(KafkaTopics.eventTransferFail, it)
                    }
                }
            }
        }
    }
}
