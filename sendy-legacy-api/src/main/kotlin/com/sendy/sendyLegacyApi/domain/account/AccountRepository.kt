package com.sendy.sendyLegacyApi.domain.account

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface AccountRepository : JpaRepository<AccountEntity, Long> {
    fun findByUserIdAndAccountNumber(
        userId: Long,
        accountNumber: String,
    ): AccountEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select account from AccountEntity account where 1=1 and account.userId = :userId")
    fun findOneBySenderUserId(userId: Long): AccountEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select account from AccountEntity account where 1=1 and account.userId = :userId")
    fun findOneByReceiverUserId(userId: Long): AccountEntity?
}
