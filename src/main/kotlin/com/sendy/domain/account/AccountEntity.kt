package com.sendy.domain.account

import com.sendy.infrastructure.persistence.Identity
import com.sendy.support.exception.account.InActiveAccountException
import com.sendy.support.exception.account.NotSufficientSendMoneyException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import java.time.LocalDateTime

@Entity
@Table(
    name = "account",
    uniqueConstraints = [
        UniqueConstraint(name = "account_account_number_uk", columnNames = ["account_number"]),
    ],
)
class AccountEntity(
    id: Long,
    @Column(name = "account_number", nullable = false, length = 13)
    val accountNumber: String,
    @Column(name = "user_id", nullable = false)
    val userId: Long,
    @Column(name = "password", nullable = false, length = 64)
    val password: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: AccountStatus,
    @Column(name = "is_primary", nullable = false)
    var isPrimary: Boolean,
    @Column(name = "is_limited_account", nullable = false)
    var isLimitedAccount: Boolean,
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime,
    @Column(name = "balance", nullable = false)
    var balance: Long,
) : Identity(id) {
    companion object {
        private const val PREFIX = "321"
        private const val MAX_LENGTH = 11

        fun generateAccountNumber(): String {
            val prefix = PREFIX
            val body = (1..<MAX_LENGTH).map { ('0'..'9').random() }.joinToString("")

            return prefix + body
        }
    }

    fun deposit(amount: Long) {
        require(amount > 0) { "입금 금액은 0보다 커야 합니다." }
        this.balance += amount
        this.updatedAt = LocalDateTime.now()
    }

    fun withdraw(amount: Long) {
        this.balance -= amount
        this.updatedAt = LocalDateTime.now()
    }

    fun checkActiveAndInvokeError() {
        if (status != AccountStatus.ACTIVE) {
            throw InActiveAccountException()
        }
    }

    fun checkRemainAmountAndInvokeError(amount: Long) {
        require(amount > 0) { "출금 금액은 0보다 커야 합니다." }
        if (balance < amount) {
            throw NotSufficientSendMoneyException()
        }
    }
}
