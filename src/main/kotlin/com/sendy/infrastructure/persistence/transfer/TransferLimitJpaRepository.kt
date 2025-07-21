package com.sendy.infrastructure.persistence.transfer

import org.springframework.data.jpa.repository.JpaRepository

interface TransferLimitJpaRepository : JpaRepository<TransferLimitJpaEntity, Long> {
    fun findByUserIdAndDailyDt(
        userId: Long,
        dailyDt: String,
    ): TransferLimitJpaEntity?
}
