package com.sendy.domain.account

import com.sendy.domain.account.TransactionHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface TransactionHistoryRepository : JpaRepository<TransactionHistoryEntity, Long> {
    //desc 로 내림
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



    @Query(
        value = """
            select th from TransactionHistoryEntity th 
            where th.createdAt >= :startDt
            and th.createdAt <= :endDt
            and th.type = 'WITHDRAW'
            order by 
                case when :sortOrder = 'asc' then th.createdAt end asc,
                case when :sortOrder = 'desc' then th.createdAt end desc
        """,
    )
    //출금
    fun findWithDrawHistory(
        @Param("startDt") startDt: LocalDateTime,
        @Param("endDt") endDt: LocalDateTime,
        @Param("sortOrder") sortOrder: String
    ): List<TransactionHistoryEntity>



    @Query(
        value = """
            select th from TransactionHistoryEntity th 
            where th.createdAt >= :startDt
            and th.createdAt <= :endDt
            and th.type = 'DEPOSIT'
            order by 
                case when :sortOrder = 'asc' then th.createdAt end asc,
                case when :sortOrder = 'desc' then th.createdAt end desc
        """,
    )
    fun findDepositHistory(
        @Param("startDt") startDt: LocalDateTime,
        @Param("endDt") endDt: LocalDateTime,
        @Param("sortOrder") sortOrder: String
    ): List<TransactionHistoryEntity>

    // 타입 조건 없이 전체 거래 내역 조회 (입금 + 출금)
    @Query(
        value = """
            select th from TransactionHistoryEntity th 
            where th.createdAt >= :startDt
            and th.createdAt <= :endDt
            order by 
                case when :sortOrder = 'asc' then th.createdAt end asc,
                case when :sortOrder = 'desc' then th.createdAt end desc
        """,
    )
    fun findAllTransactionHistory(
        @Param("startDt") startDt: LocalDateTime,
        @Param("endDt") endDt: LocalDateTime,
        @Param("sortOrder") sortOrder: String
    ): List<TransactionHistoryEntity>


}
