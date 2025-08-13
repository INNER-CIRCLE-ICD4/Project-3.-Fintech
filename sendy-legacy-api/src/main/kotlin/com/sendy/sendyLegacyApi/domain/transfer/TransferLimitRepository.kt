package com.sendy.sendyLegacyApi.domain.transfer

import org.springframework.data.jpa.repository.JpaRepository

interface TransferLimitRepository : JpaRepository<TransferLimitEntity, Long> {
    fun findByUserIdAndDailyDt(
        userId: Long,
        dailyDt: String,
    ): TransferLimitEntity?
}
