package com.sendy.domain.account

import com.sendy.domain.account.TransactionHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface TransactionHistoryRepository : JpaRepository<TransactionHistoryEntity, Long> {
    @Query(
        value = """
            select th from TransactionHistoryEntity th 
            where th.createdAt >= :startDt
            and th.createdAt <= :endDt
            and th.type = 'WITHDRAW'
            order by th.createdAt desc
        """,
    )
    fun findWithDrawToday(
        @Param("startDt") startDt: LocalDateTime,
        @Param("endDt") endDt: LocalDateTime,
    ): List<TransactionHistoryEntity>
}
