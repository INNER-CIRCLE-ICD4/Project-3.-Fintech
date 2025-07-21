package com.sendy.domain.transfer

interface TransactionHistoryRepository {
    fun save(domain: TransactionHistory)
}
