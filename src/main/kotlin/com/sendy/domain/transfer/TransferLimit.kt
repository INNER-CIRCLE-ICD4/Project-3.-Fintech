package com.sendy.domain.transfer

import java.time.LocalDateTime

data class TransferLimit(
    val id: Long,
    val dailyDt: String,
    val dailyLimit: Long,
    val singleTransactionLimit: Long,
    var dailyCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val userId: Long,
) {
    fun incrementDailyCount(
        withdrawTxList: List<TransactionHistory>,
        amount: Int,
    ) = if (withdrawTxList.isEmpty() && amount.toLong() <= dailyLimit) {
        dailyCount++
    } else {
        throw Exception("이체 한도를 초과했습니다.")
    }
}
