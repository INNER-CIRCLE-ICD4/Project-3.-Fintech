package com.sendy.transferDomain.domain

import java.time.LocalDateTime

interface TransferRepository {
    fun getReservedTransferByCursor(
        startDt: LocalDateTime,
        endDt: LocalDateTime,
        id: Long? = null,
        fetchSize: Int? = 1_000,
    ): ReservationTransfer
}
