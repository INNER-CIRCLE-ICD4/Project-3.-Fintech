package com.sendy.domain.account

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query

interface AccountRepository : JpaRepository<AccountEntity, Long> {
    @Query(
        "select account " +
            "from AccountEntity account " +
            "where account.userId = :userId " +
            "and account.status='ACTIVE' " +
            "and account.isPrimary = true and account.isLimitedAccount = false",
    )
    fun findByActive(userId: Long): AccountEntity?

    fun findByUserIdAndAccountNumber(
        userId: Long,
        accountNumber: String,
    ): AccountEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select account from AccountEntity account where 1=1 and account.accountNumber = :accountNumber")
    fun findOneBySenderAccountNumber(accountNumber: String): AccountEntity?

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select account from AccountEntity account where 1=1 and account.accountNumber = :accountNumber")
    fun findOneByReceiveAccountNumber(accountNumber: String): AccountEntity?
}
