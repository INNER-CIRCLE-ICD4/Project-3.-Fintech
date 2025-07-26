package com.sendy.domain.model

import com.sendy.domain.account.AccountStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AccountTest {

    @Test
    fun `계좌번호가 13자리가 아니면 예외 발생`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Account.create("123", 1L)
        }
        assertEquals("계좌번호는 13자리여야 합니다.", ex.message)
    }

    @Test
    fun `계좌번호에 숫자 외 문자 포함시 예외 발생`() {
        val ex = assertThrows(IllegalArgumentException::class.java) {
            Account.create("3211234567A90", 1L)
        }
        assertEquals("계좌번호는 숫자만 가능합니다.", ex.message)
    }

    @Test
    fun `계좌 생성 성공`() {
        val account = Account.create(
            accountNumber = "3211234567890",
            userId = 123L
        )

        assertEquals("3211234567890", account.accountNumber)
        assertEquals(123L, account.userId)
        assertEquals(AccountStatus.ACTIVE, account.status)
        assertTrue(account.isLimitedAccount)
        assertFalse(account.isPrimary)
        assertEquals(0L, account.balance)
    }

}
