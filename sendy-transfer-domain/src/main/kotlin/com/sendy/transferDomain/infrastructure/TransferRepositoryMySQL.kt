package com.sendy.transferDomain.infrastructure

import com.sendy.transferDomain.domain.Transfer
import com.sendy.transferDomain.domain.TransferRepository
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
internal class TransferRepositoryMySQL(
    private val transferJpaRepository: TransferJpaRepository,
    private val jdbcClient: JdbcClient,
) : TransferRepository {
    override fun findReservedTransfer(
        start: LocalDateTime,
        end: LocalDateTime,
    ): List<Transfer> =
        transferJpaRepository.findReserved(start, end).map {
            Transfer(
                id = it.id,
                sendUserId = it.sendUserId,
                sendAccountNumber = it.sendAccountNumber,
                receivePhoneNumber = it.receivePhoneNumber,
                receiveAccountNumber = it.receiveAccountNumber,
                amount = it.amount,
                status = it.status,
                scheduledAt = it.scheduledAt,
                requestedAt = it.requestedAt,
                completedAt = it.completedAt,
                reason = it.reason,
            )
        }

    override fun getReservedTransferByCursor(
        id: Long,
        startDt: LocalDateTime,
        endDt: LocalDateTime,
    ): List<Transfer> {
        val totalCount =
            jdbcClient
                .sql(
                    """
                    select count(*) 
                    from transfers 
                    where id > :id 
                    and scheduled_at >= :startDt 
                    and scheduled_at <= :endDt
                    """.trimIndent(),
                ).params(id, startDt, endDt)
                .query(Int::class.java)
                .single()
        TODO("Not yet implemented")
    }
}
