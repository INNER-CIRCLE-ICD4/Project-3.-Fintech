package com.sendy.infrastructure.persistence.account

import com.sendy.domain.account.TransactionHistory
import com.sendy.domain.account.TransactionHistoryRepository
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
