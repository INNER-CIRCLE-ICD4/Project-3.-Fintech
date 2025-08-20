package com.sendy.transferDomain.infrastructure

import com.sendy.transferDomain.domain.ReservationTransfer
import com.sendy.transferDomain.domain.TransferRepository
import com.sendy.transferDomain.domain.vo.TransferId
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
internal class TransferRepositoryMySQL(
    private val transferJpaRepository: TransferJpaRepository,
    private val jdbcTemplate: JdbcTemplate,
) : TransferRepository {
    override fun getReservedTransferByCursor(
        startDt: LocalDateTime,
        endDt: LocalDateTime,
        id: Long?,
        fetchSize: Int?,
    ): ReservationTransfer {
        val limit = fetchSize ?: 1_000

        val jdbcClient = JdbcClient.create(jdbcTemplate)
        val totalCount =
            jdbcClient
                .sql(countQuery)
                .param("startDt", startDt)
                .param("endDt", endDt)
                .query(Int::class.java)
                .single()

        val selectQb =
            jdbcClient
                .sql(selectQuery(id, limit))
                .param("startDt", startDt)
                .param("endDt", endDt)

        id?.let { selectQb.param("id", it) }

        val list =
            selectQb
                .query { rs, _ ->
                    TransferId(rs.getLong("id"))
                }.list()

        val nextCursor = if (list.isNotEmpty()) list.last() else null

        return ReservationTransfer(totalCount, nextCursor, list)
    }

    private val countQuery =
        "select count(*) " +
            "from transfer " +
            "where scheduled_at >= :startDt " +
            "and scheduled_at <= :endDt " +
            "and status = 'RESERVE' " +
            "and completed_at is null"

    private fun selectQuery(
        id: Long?,
        limit: Int,
    ) = id?.let {
        "select id " +
            "from transfer " +
            "where 1=1 " +
            "and id < :id " +
            "and scheduled_at >= :startDt " +
            "and scheduled_at <= :endDt " +
            "and status = 'RESERVE' " +
            "and completed_at is null " +
            "order by id desc " +
            "limit $limit"
    }
        ?: (
            "select id " +
                "from transfer " +
                "where 1=1 " +
                "and scheduled_at >= :startDt " +
                "and scheduled_at <= :endDt " +
                "and status = 'RESERVE' " +
                "and completed_at is null " +
                "order by id desc " +
                "limit $limit"
        )
}
