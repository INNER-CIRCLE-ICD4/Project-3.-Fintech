package com.sendy.transferDomain.domain

interface TransferRepository {
    fun getReservedTransferByCursor(
        startDt: String,
        endDt: String,
        id: Long? = null,
        fetchSize: Int? = 1_000,
    ): ReservationTransfer
}
