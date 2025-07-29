package com.sendy.application.facade.account

import com.sendy.application.dto.account.AccountBalanceResponse
import com.sendy.application.usecase.account.command.SaveTransactionHistoryUseCase
import com.sendy.application.usecase.account.command.dto.TransactionHistoryDto
import com.sendy.application.usecase.account.query.QueryAccountBalanceUseCase
import com.sendy.domain.enum.TransactionHistoryTypeEnum
import com.sendy.domain.enum.TransactionHistoryTypeEnum.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountFacade(
    private val saveTransactionHistoryUseCase: SaveTransactionHistoryUseCase,
    private val queryAccountBalanceUseCase: QueryAccountBalanceUseCase,
) {
    @Transactional
    fun processTransaction(
        userId: Long,
        accountNumber: String,
        amount: Long,
        type: TransactionHistoryTypeEnum,
        description: String,
        transferId: Long? = null
    ): AccountBalanceResponse {
        return queryAccountBalanceUseCase.execute(userId, accountNumber).run {
            when (type) {
                DEPOSIT -> deposit(amount)
                WITHDRAW -> withdraw(amount)
            }
            saveTransactionHistoryUseCase.execute(
                TransactionHistoryDto(
                    accountId = id,
                    type = type,
                    amount = amount,
                    balanceAfter = balance,
                    description = description,
                    transferId = transferId
                )
            )
            AccountBalanceResponse(accountNumber = accountNumber, balance = balance)
        }
    }
}
