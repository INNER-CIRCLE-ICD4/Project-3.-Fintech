package com.sendy.domain.account

import com.sendy.support.error.TransferErrorCode
import com.sendy.support.exception.ServiceException
import com.sendy.support.util.SHA256Util
import com.sendy.support.util.getTsid
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals

class AccountPasswordMatchesTest {
    private val sha256Util = SHA256Util()

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
                password = sha256Util.hash("abcde"),
                status = AccountStatus.ACTIVE,
                isPrimary = true,
                isLimitedAccount = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                balance = 0L,
            )

        val transferException =
            assertThrows<ServiceException> {
                privateMatchesFunc(actualPassword, accountEntity.password)
            }

        assertEquals(transferException.message, TransferErrorCode.INVALID_ACCOUNT_NUMBER_PASSWORD.description)
    }

    private fun privateMatchesFunc(
        actualPassword: String,
        accountPassword: String,
    ) {
        val matches = sha256Util.matches(actualPassword, accountPassword)

        if (matches.not()) {
            throw ServiceException(TransferErrorCode.INVALID_ACCOUNT_NUMBER_PASSWORD)
        }
    }
}
