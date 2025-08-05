package com.sendy.domain.account

import com.sendy.infrastructure.persistence.Identity
import com.sendy.support.error.TransferErrorCode
import com.sendy.support.exception.ServiceException
import com.sendy.support.exception.account.InActiveAccountException
import com.sendy.support.exception.account.NotSufficientSendMoneyException
import com.sendy.support.util.Aes256Util
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
    fun deposit(amount: Long) {
        require(amount > 0) { "입금 금액은 0보다 커야 합니다." }
        this.balance += amount
        this.updatedAt = LocalDateTime.now()
    }

    fun withdraw(amount: Long) {
        this.balance -= amount
        this.updatedAt = LocalDateTime.now()
    }

    fun checkPasswordAndInvokeError(
        password: String,
        key: String,
    ) {
        val aes256Util = Aes256Util(key)

        val decrypt = aes256Util.decrypt(this.password)

        if (password != decrypt) {
            throw ServiceException(TransferErrorCode.INVALID_ACCOUNT_NUMBER_PASSWORD)
        }
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
