package com.sendy.sendyLegacyApi.domain.transfer

import java.time.LocalDateTime

interface TransferLimitCountProcessor {
    fun processLimitCount(
        userId: Long,
        dailyDt: LocalDateTime,
        amount: Long,
    )
}
