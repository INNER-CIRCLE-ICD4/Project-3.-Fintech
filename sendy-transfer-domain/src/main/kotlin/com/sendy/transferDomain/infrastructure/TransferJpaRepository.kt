package com.sendy.transferDomain.infrastructure

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

internal interface TransferJpaRepository : JpaRepository<TransferEntity, Long> {
    @Query(
        value =
            "select transfer " +
                "from TransferEntity transfer " +
                "where 1 = 1 " +
                "and transfer.status='PENDING' " +
                "and transfer.scheduledAt >= :startDt " +
                "and transfer.scheduledAt <= :endDt " +
                "and transfer.completedAt is null",
        countQuery =
            "select count (transfer) " +
                "from TransferEntity transfer " +
                "where 1 = 1 " +
                "and transfer.status='PENDING' " +
                "and transfer.scheduledAt >= :startDt " +
                "and transfer.scheduledAt <= :endDt " +
                "and transfer.completedAt is null",
    )
    fun findReserved(
        startDt: LocalDateTime,
        endDt: LocalDateTime,
    ): List<TransferEntity>
}
