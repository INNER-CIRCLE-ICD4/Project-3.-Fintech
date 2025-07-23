package com.sendy.infrastructure.persistence.account

import com.sendy.domain.account.AccountStatus
import com.sendy.domain.account.Account
import com.sendy.infrastructure.persistence.Identity
import com.sendy.support.util.getTsid
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "account")
class AccountEntity(
    id: Long,

    @Column(name = "account_number", nullable = false, length = 13)
    val accountNumber: String,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: AccountStatus,

    @Column(name = "is_primary", nullable = false)
    val isPrimary: Boolean,

    @Column(name = "is_limited_account", nullable = false)
    val isLimitedAccount: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "balance", nullable = false)
    val balance: Long
) : Identity(id) {
    fun toModel() = Account(
        accountNumber = this.accountNumber,
        userId = this.userId,
        status = this.status,
        isPrimary = this.isPrimary,
        isLimitedAccount = this.isLimitedAccount,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        balance = this.balance
    )

    companion object {
        fun from(account: Account): AccountEntity {
            return AccountEntity(
                id = getTsid(),
                accountNumber = account.accountNumber,
                userId = account.userId,
                status = account.status,
                isPrimary = account.isPrimary,
                isLimitedAccount = account.isLimitedAccount,
                createdAt = account.createdAt,
                updatedAt = account.updatedAt,
                balance = account.balance
            )
        }
    }
}

