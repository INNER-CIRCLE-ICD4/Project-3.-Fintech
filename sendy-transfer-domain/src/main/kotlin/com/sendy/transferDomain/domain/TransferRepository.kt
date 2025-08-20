package com.sendy.transferDomain.domain

import java.time.LocalDateTime

interface TransferRepository {
    fun findReservedTransfer(
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<Transfer>

    fun getReservedTransferByCursor(
        startDt: LocalDateTime,
        endDt: LocalDateTime,
        id: Long? = null,
        fetchSize: Int? = 1_000,
    ): ReservationTransfer
}
