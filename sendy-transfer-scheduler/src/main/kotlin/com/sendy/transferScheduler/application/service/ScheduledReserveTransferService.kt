package com.sendy.transferScheduler.application.service

import com.sendy.sharedKafka.event.EventPublisher
import com.sendy.transferDomain.domain.TransferRepository
import com.sendy.transferDomain.domain.vo.TransferId
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Service
class ScheduledReserveTransferService(
    private val transferRepository: TransferRepository,
    private val eventPublisher: EventPublisher,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "1h")
    fun scheduled() {
        val topic = "transfer-scheduler.transfer.reservation.started"
        val dateNow = LocalDateTime.now()
        val truncatedTo = dateNow.truncatedTo(ChronoUnit.HOURS)
        val startDt = "${truncatedTo.minus(1, ChronoUnit.HOURS)}:00"
        val endDt = "$truncatedTo:00"

        val readReservationTransfer = readReservationTransfer(startDt, endDt)

        readReservationTransfer.forEach {
            logger.info("reservation publish, size: {}", it.size)

            eventPublisher.publish(topic, it)
        }
    }

    private fun readReservationTransfer(
        startDt: String,
        endDt: String,
    ): List<List<TransferId>> {
        val fetchSize = 100
        val list = mutableListOf<List<TransferId>>()

        val initResult = transferRepository.getReservedTransferByCursor(startDt, endDt, fetchSize = fetchSize)
        var nextCursor = initResult.nextCursor

        list.add(initResult.transferIds)

        while (nextCursor != null) {
            val next = transferRepository.getReservedTransferByCursor(startDt, endDt, fetchSize = fetchSize, id = nextCursor.id)
            nextCursor = next.nextCursor

            next.nextCursor?.let { list.add(next.transferIds) }
        }

        return list
    }
}
