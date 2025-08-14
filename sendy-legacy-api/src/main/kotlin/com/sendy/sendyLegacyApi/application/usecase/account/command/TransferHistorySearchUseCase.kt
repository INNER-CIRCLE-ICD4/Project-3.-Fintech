package com.sendy.sendyLegacyApi.application.usecase.account.command

import com.sendy.sendyLegacyApi.application.dto.account.TransferHistorySearchRequest
import com.sendy.sendyLegacyApi.application.dto.account.TransferHistorySearchResponse
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
interface TransferHistorySearchUseCase {
    fun execute(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
    ): TransferHistorySearchResponse

    fun executeList(request: TransferHistorySearchRequest): List<TransferHistorySearchResponse>

    fun findDepositHistory(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sortOrder: String,
    ): List<TransferHistorySearchResponse>

    fun findWithdrawHistory(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sortOrder: String,
    ): List<TransferHistorySearchResponse>

    fun findAllTransactionHistory(
        userId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        sortOrder: String,
    ): List<TransferHistorySearchResponse>
}
