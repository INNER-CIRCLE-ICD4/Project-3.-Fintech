package com.sendy.application.usecase.account.command

import com.sendy.application.usecase.account.command.dto.TransactionHistoryDto
import com.sendy.domain.account.TransactionHistoryEntity
import com.sendy.infrastructure.persistence.account.TransactionHistoryRepository
import com.sendy.support.util.getTsid
import org.springframework.stereotype.Service

@Service
class SaveTransactionHistoryService(
    private val transactionHistoryRepository: TransactionHistoryRepository
) : SaveTransactionHistoryUseCase {
    override fun execute(dto: TransactionHistoryDto): TransactionHistoryEntity {
        return transactionHistoryRepository.save(
            TransactionHistoryEntity(
                id = getTsid(),
                type = dto.type,
                amount = dto.amount,
                balanceAfter = dto.balanceAfter,
                description = dto.description,
                createdAt = dto.createdAt,
                transferId = dto.transferId
            )
        )
    }
}

