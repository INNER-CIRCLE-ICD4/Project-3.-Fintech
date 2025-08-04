package com.sendy.domain.account

import com.sendy.support.error.TransferErrorCode
import com.sendy.support.exception.ServiceException
import com.sendy.support.util.Aes256Util
import com.sendy.support.util.getTsid
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AccountPasswordMatchesTest {
    private val key = "12345678901234567890123456789012"
    private val aes256Util = Aes256Util(key)

    @Test
    fun `계좌 비밀번호 검증 시 불일치 예외를 발생시켜야된다`() {
        val testUserId = 1L
        val actualPassword = "12345"
        val generatedAccountNumber = "3219876543210"

        val accountEntity =
            AccountEntity(
                id = getTsid(),
                accountNumber = generatedAccountNumber,
                userId = testUserId,
                password = aes256Util.encrypt("abcde"),
                status = AccountStatus.ACTIVE,
                isPrimary = true,
                isLimitedAccount = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                balance = 0L,
            )

        val transferException =
            assertThrows<ServiceException> {
                accountEntity.checkPasswordAndInvokeError(actualPassword, key)
            }

        assertEquals(transferException.message, TransferErrorCode.INVALID_ACCOUNT_NUMBER_PASSWORD.description)
    }
}
