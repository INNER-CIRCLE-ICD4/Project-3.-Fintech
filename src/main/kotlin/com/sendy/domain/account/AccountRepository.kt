package com.sendy.domain.account

import com.sendy.domain.account.AccountEntity
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface AccountRepository : JpaRepository<AccountEntity, Long> {
    fun findByUserId(userId: Long): List<AccountEntity>

    fun findByUserIdAndAccountNumber(
        userId: Long,
        accountNumber: String,
    ): AccountEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select account from AccountEntity account where 1=1 and account.accountNumber = :accountNumber")
    fun findOneByAccountNumberForUpdate(accountNumber: String): AccountEntity
}
