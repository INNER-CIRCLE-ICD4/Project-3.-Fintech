package com.sendy.infrastructure.persistence.transfer

import com.sendy.domain.transfer.Transfer
import com.sendy.domain.transfer.TransferRepository
import org.springframework.stereotype.Repository

@Repository
class TransferMysqlRepository(
    private val transferJpaRepository: TransferJpaRepository,
) : TransferRepository {
    override fun save(domain: Transfer) {
        transferJpaRepository.save(
            TransferJpaEntity(
                id = domain.id,
                status = domain.status,
                amount = domain.amount,
                requestedAt = domain.requestedAt,
                completedAt = domain.completedAt,
                reason = domain.reason,
                scheduledAt = domain.scheduledAt,
            ),
        )
    }

    override fun getTransferById(id: Long): Transfer {
        val entity = transferJpaRepository.findById(id).orElseThrow()

        return Transfer(
            id = entity.id,
            amount = entity.amount,
            status = entity.status,
            scheduledAt = entity.scheduledAt,
            requestedAt = entity.requestedAt,
            completedAt = entity.completedAt,
            reason = entity.reason,
        )
    }
}
