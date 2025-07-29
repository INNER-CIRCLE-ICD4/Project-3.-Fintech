package com.sendy.domain.account

import com.sendy.domain.account.AccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<AccountEntity, Long> {
    fun findByUserId(userId: Long): List<AccountEntity>

    fun findByUserIdAndAccountNumber(
        userId: Long,
        accountNumber: String,
    ): AccountEntity?
}
