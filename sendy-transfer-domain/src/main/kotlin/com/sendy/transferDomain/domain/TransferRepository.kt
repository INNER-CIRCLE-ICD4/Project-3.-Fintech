package com.sendy.transferDomain.domain

import java.time.LocalDateTime

interface TransferRepository {
    fun findReservedTransfer(
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<Transfer>
}
