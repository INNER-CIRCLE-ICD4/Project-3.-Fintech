package com.sendy.transferScheduler.application.service

import com.sendy.transferDomain.domain.TransferRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class ScheduledReserveTransferService(
    private val transferRepository: TransferRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Scheduled(fixedDelayString = "10s")
    fun scheduled() {
        val dateNow = LocalDate.now()
        val start = LocalDateTime.of(dateNow, LocalTime.of(0, 0, 0))
        val end = LocalDateTime.of(dateNow, LocalTime.of(23, 59, 59))
        val findReservedTransfer = transferRepository.findReservedTransfer(start, end)
        logger.info("scheduled.. start")

        findReservedTransfer.forEach {
            logger.info(
                "id: {}, amount: {}, status: {}, scheduledAt: {}, completedAt: {}",
                it.id,
                it.amount,
                it.status,
                it.scheduledAt,
                it.completedAt,
            )
        }

        logger.info("scheduled.. end")
    }
}
