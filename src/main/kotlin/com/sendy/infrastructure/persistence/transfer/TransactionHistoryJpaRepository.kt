package com.sendy.infrastructure.persistence.transfer

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface TransactionHistoryJpaRepository :
    JpaRepository<
        TransactionHistoryJpaEntity,
        Long,
    > {
    @Query(
        value = """
            select th from TransactionHistoryJpaEntity th 
            where th.createdAt >= :startDt
            and th.createdAt <= :endDt
            and th.type = 'WITHDRAW'
            order by th.createdAt desc
        """,
    )
    fun findWithDrawToday(
        @Param("startDt") startDt: LocalDateTime,
        @Param("endDt") endDt: LocalDateTime,
    ): List<TransactionHistoryJpaEntity>
}
