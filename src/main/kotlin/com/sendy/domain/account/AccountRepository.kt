package com.sendy.domain.account

interface AccountRepository {
    fun save(account: Account): Account
    fun findById(id: Long): Account?
    fun findByUserId(userId: Long): List<Account>
    fun findByUserIdAndAccountNumber(userId: Long, accountNumber: String): Account?
}
