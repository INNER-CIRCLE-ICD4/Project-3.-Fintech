package com.sendy.domain.account

interface TransactionHistoryRepository {
    fun save(domain: TransactionHistory)
}
