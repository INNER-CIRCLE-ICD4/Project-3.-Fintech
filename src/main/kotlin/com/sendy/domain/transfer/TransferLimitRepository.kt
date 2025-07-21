package com.sendy.domain.transfer

interface TransferLimitRepository {
    fun getTransferLimitBy(
        userId: Long,
        dailyDt: String,
    ): TransferLimit?

    fun save(domain: TransferLimit): TransferLimit
}
