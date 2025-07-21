package com.sendy.domain.transfer

interface TransferLimitCountProcessor {
    fun processLimitCount(
        transactions: List<TransactionHistory>,
        userId: Long,
        dailyDt: String,
        callback: (withdrawTxList: List<TransactionHistory>, transferLimit: TransferLimit) -> Unit,
    ): TransferLimit
}
