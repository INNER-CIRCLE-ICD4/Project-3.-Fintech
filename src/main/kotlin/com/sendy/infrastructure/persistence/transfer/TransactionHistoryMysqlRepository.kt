package com.sendy.infrastructure.persistence.transfer

import com.sendy.domain.transfer.TransactionHistory
import com.sendy.domain.transfer.TransactionHistoryRepository
import org.springframework.stereotype.Repository

@Repository
class TransactionHistoryMysqlRepository(
    private val transactionHistoryJpaRepository: TransactionHistoryJpaRepository,
) : TransactionHistoryRepository {
    override fun save(domain: TransactionHistory) {
        transactionHistoryJpaRepository.save(
            TransactionHistoryJpaEntity(
                id = domain.id,
                amount = domain.amount,
                type = domain.type,
                balanceAfter = domain.balanceAfter,
                description = domain.description,
                createdAt = domain.createdAt,
                transferId = domain.transferId,
            ),
        )
    }
}
