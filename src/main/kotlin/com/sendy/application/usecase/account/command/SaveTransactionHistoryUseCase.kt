package com.sendy.application.usecase.account.command

import com.sendy.application.usecase.account.command.dto.TransactionHistoryDto
import com.sendy.domain.account.TransactionHistoryEntity

interface SaveTransactionHistoryUseCase {
    fun execute(dto: TransactionHistoryDto): TransactionHistoryEntity
}

