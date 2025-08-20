package com.sendy.transferScheduler.application.service

import com.sendy.sharedKafka.event.EventMessage
import com.sendy.sharedKafka.event.EventPublisher
import com.sendy.transferDomain.domain.TransferRepository
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduledReserveTransferService(
    private val transferRepository: TransferRepository,
    private val eventPublisher: EventPublisher,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "1h")
    fun scheduled() {
        eventPublisher.publish(
            "transfer-scheduler.transfer.reservation.started",
            EventMessage(id = 1L, source = "test", aggregateId = 1234L, payload = "test"),
        )
    }

    @KafkaListener(topics = ["transfer-scheduler.transfer.reservation.started"])
    fun consumer(message: EventMessage<String>) {
        logger.info("self consume message: {}", message)
    }
}
