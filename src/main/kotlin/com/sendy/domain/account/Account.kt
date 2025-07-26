package com.sendy.domain.account

import java.time.LocalDateTime

class Account(
    val accountNumber: String,
    val userId: Long,
    val status: AccountStatus,
    val isPrimary: Boolean,
    val isLimitedAccount: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val balance: Long
) {
    companion object {
        /**
         * 계좌 생성 팩토리
         * - 13자리 숫자 계좌번호 : 321(기관코드)+10자리
         */

        fun create(
            accountNumber: String,
            userId: Long,
            isPrimary: Boolean = false,
            isLimitedAccount: Boolean = true
        ): Account {
            require(accountNumber.length == 13) { "계좌번호는 13자리여야 합니다." }
            require(accountNumber.all { it.isDigit() }) { "계좌번호는 숫자만 가능합니다." }

            return Account(
                accountNumber = accountNumber,
                userId = userId,
                status = AccountStatus.ACTIVE,
                isPrimary = isPrimary,
                isLimitedAccount = isLimitedAccount,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                balance = 0L
            )
        }
    }
}
