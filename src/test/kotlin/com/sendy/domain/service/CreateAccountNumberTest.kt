package com.sendy.domain.service

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.usecase.account.CreateAccountService
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountRepository
import com.sendy.domain.account.AccountStatus
import com.sendy.support.util.SHA256Util
import com.sendy.support.util.getTsid
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DisplayName("계좌 생성 서비스 테스트")
class CreateAccountNumberTest {
    private val accountRepository = mockk<AccountRepository>()
    private val sha256Util = mockk<SHA256Util>()

    private lateinit var createAccountService: CreateAccountService

    private lateinit var testRequest: CreateAccountRequest
    private lateinit var testAccountEntity: AccountEntity
    private val testUserId = 1L
    private val testPassword = "1111"
    private val generatedAccountNumber = "3219876543210"

    @BeforeEach
    fun setUp() {
        createAccountService =
            CreateAccountService(
                accountRepository,
                sha256Util,
            )

        testRequest =
            CreateAccountRequest(
                userId = testUserId,
                password = testPassword,
                initBalance = 1_000L,
            )

        testAccountEntity =
            AccountEntity(
                id = getTsid(),
                accountNumber = generatedAccountNumber,
                userId = testUserId,
                password = "encryptedPassword",
                status = AccountStatus.ACTIVE,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                balance = 0L,
            )
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    @DisplayName("계좌 생성이 성공적으로 완료되어야 한다")
    fun `계좌 생성 성공 테스트`() {
        // given
        every { sha256Util.hash(testPassword) } returns "encryptedPassword"
        every { accountRepository.save(any<AccountEntity>()) } returns testAccountEntity

        // when
        val result = createAccountService.execute(testRequest)

        // then
        assertNotNull(result)
        assertEquals(generatedAccountNumber, result.accountNumber)

        verify(exactly = 1) { sha256Util.hash(testPassword) }
        verify(exactly = 1) { accountRepository.save(any<AccountEntity>()) }
    }

    @Test
    @DisplayName("계좌 생성 시 올바른 AccountEntity가 저장되어야 한다")
    @Ignore
    fun `계좌 생성 시 AccountEntity 검증 테스트`() {
        // given
        val savedEntitySlot = slot<AccountEntity>()

        every { sha256Util.hash(testPassword) } returns "encryptedPassword"
        every { accountRepository.save(capture(savedEntitySlot)) } returns testAccountEntity

        // when
        createAccountService.execute(testRequest)

        // then
        val savedEntity = savedEntitySlot.captured
        verify { accountRepository.save(savedEntity) }

        assertEquals(testAccountEntity, savedEntity)
    }

    @Test
    @DisplayName("계좌 저장이 호출되어야 한다")
    fun `계좌 저장 호출 테스트`() {
        // given
        every { sha256Util.hash(testPassword) } returns "encryptedPassword"
        every { accountRepository.save(any<AccountEntity>()) } returns testAccountEntity

        // when
        createAccountService.execute(testRequest)

        // then
        verify(exactly = 1) { accountRepository.save(any<AccountEntity>()) }
    }

    @Test
    @DisplayName("생성된 계좌번호가 321로 시작하는 13자리여야 한다")
    fun `계좌번호 321 형식 검증 테스트`() {
        // given
        val savedEntitySlot = slot<AccountEntity>()

        every { sha256Util.hash(testPassword) } returns "encryptedPassword"
        every { accountRepository.save(capture(savedEntitySlot)) } returns testAccountEntity

        // when
        createAccountService.execute(testRequest)

        // then
        verify { accountRepository.save(any<AccountEntity>()) }

        val savedEntity = savedEntitySlot.captured
        assertEquals(13, savedEntity.accountNumber.length)
        assertTrue(savedEntity.accountNumber.all { it.isDigit() })
        assertTrue(savedEntity.accountNumber.startsWith("321"))
    }

    @Test
    @DisplayName("계좌번호가 숫자로만 구성되어야 한다")
    fun `계좌번호 숫자 검증 테스트`() {
        // given
        val savedEntitySlot = slot<AccountEntity>()

        every { sha256Util.hash(testPassword) } returns "encryptedPassword"
        every { accountRepository.save(capture(savedEntitySlot)) } returns testAccountEntity

        // when
        createAccountService.execute(testRequest)

        // then
        verify { accountRepository.save(any<AccountEntity>()) }

        val savedEntity = savedEntitySlot.captured
        assertTrue(savedEntity.accountNumber.all { it.isDigit() })
    }

    @Test
    @DisplayName("13자리가 아닌 계좌번호는 예외를 발생시켜야 한다")
    @Ignore
    fun `13자리가 아닌 계좌번호 예외 테스트`() {
        // given

        // when & then
        assertThrows<IllegalArgumentException> {
            createAccountService.execute(testRequest)
        }
    }

    @Test
    @DisplayName("숫자가 아닌 문자가 포함된 계좌번호는 예외를 발생시켜야 한다")
    @Ignore
    fun `숫자가 아닌 문자 포함 계좌번호 예외 테스트`() {
        // given

        // when & then
        assertThrows<IllegalArgumentException> {
            createAccountService.execute(testRequest)
        }
    }

    @Test
    @DisplayName("321로 시작하지 않는 계좌번호는 예외를 발생시켜야 한다")
    @Ignore
    fun `321로 시작하지 않는 계좌번호 예외 테스트`() {
        // given
        // when & then
        assertThrows<IllegalArgumentException> {
            createAccountService.execute(testRequest)
        }
    }

    @Test
    @DisplayName("빈 문자열 계좌번호는 예외를 발생시켜야 한다")
    @Ignore
    fun `빈 문자열 계좌번호 예외 테스트`() {
        // given
        // when & then
        assertThrows<IllegalArgumentException> {
            createAccountService.execute(testRequest)
        }
    }
}
