package com.sendy.transferScheduler.application.service

import com.sendy.transferDomain.domain.TransferRepository
import com.sendy.transferDomain.domain.vo.TransferId
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
        val fetchSize = 100
        val dateNow = LocalDate.now()
        val start = LocalDateTime.of(dateNow, LocalTime.of(0, 0, 0))
        val end = LocalDateTime.of(dateNow, LocalTime.of(23, 59, 59))
    }

    private fun readReservationTransfer(
        startDt: LocalDateTime,
        endDt: LocalDateTime,
        fetchSize: Int,
    ): List<List<TransferId>> {
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
