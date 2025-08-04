package com.sendy.domain.service

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.usecase.account.command.CreateAccountService
import com.sendy.application.usecase.account.command.GeneratedAccountNumberUseCase
import com.sendy.application.usecase.account.CreateAccountEntityService
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountRepository
import com.sendy.domain.account.AccountStatus
import com.sendy.support.util.Aes256Util
import com.sendy.support.util.getTsid
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
@DisplayName("계좌 생성 서비스 테스트")
class CreateAccountNumberTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var generateAccountNumberUseCase: GeneratedAccountNumberUseCase

    @Mock 
    private lateinit var createAccountEntityService: CreateAccountEntityService

    @Mock
    private lateinit var aes256Util: Aes256Util

    @InjectMocks
    private lateinit var createAccountService: CreateAccountService

    private lateinit var testRequest: CreateAccountRequest
    private lateinit var testAccountEntity: AccountEntity
    private val testUserId = 1L
    private val testPassword = "1111"
    private val generatedAccountNumber = "3219876543210"

        @BeforeEach
    fun setUp() {
        testRequest = CreateAccountRequest(
            userId = testUserId,
            password = testPassword,
            isPrimary = true,
            isLimitedAccount = false
        )

        testAccountEntity = AccountEntity(
            id = getTsid(),
            accountNumber = generatedAccountNumber,
            userId = testUserId,
            password = testPassword,
            status = AccountStatus.ACTIVE,
            isPrimary = true,
            isLimitedAccount = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            balance = 0L
        )
    }

    @Test
    @DisplayName("계좌 생성이 성공적으로 완료되어야 한다")
    fun `계좌 생성 성공 테스트`() {
        // given
        `when`(generateAccountNumberUseCase.execute()).thenReturn(generatedAccountNumber)
        `when`(aes256Util.encrypt(testPassword)).thenReturn("encryptedPassword")
        `when`(createAccountEntityService.execute(any(), any(), any())).thenReturn(testAccountEntity)
        `when`(accountRepository.save(any(AccountEntity::class.java))).thenReturn(testAccountEntity)

        // when
        val result = createAccountService.execute(testRequest)

        // then
        assertNotNull(result)
        assertEquals(generatedAccountNumber, result.accountNumber)
        verify(generateAccountNumberUseCase, times(1)).execute()
        verify(aes256Util, times(1)).encrypt(testPassword)
        verify(createAccountEntityService, times(1)).execute(any(), any(), any())
        verify(accountRepository, times(1)).save(any(AccountEntity::class.java))
    }

    @Test
    @DisplayName("계좌 생성 시 올바른 AccountEntity가 저장되어야 한다")
    fun `계좌 생성 시 AccountEntity 검증 테스트`() {
        // given
        `when`(generateAccountNumberUseCase.execute()).thenReturn(generatedAccountNumber)
        `when`(aes256Util.encrypt(testPassword)).thenReturn("encryptedPassword")
        `when`(createAccountEntityService.execute(any(), any(), any())).thenReturn(testAccountEntity)
        `when`(accountRepository.save(any(AccountEntity::class.java))).thenReturn(testAccountEntity)

        // when
        createAccountService.execute(testRequest)

        // then
        val captor = ArgumentCaptor.forClass(AccountEntity::class.java)
        verify(accountRepository).save(captor.capture())
        
        val savedEntity = captor.value
        assertEquals(generatedAccountNumber, savedEntity.accountNumber) // 서버에서 생성된 계좌번호
        assertEquals(testUserId, savedEntity.userId)
        assertEquals(AccountStatus.ACTIVE, savedEntity.status)
        assertEquals(true, savedEntity.isPrimary)
        assertEquals(false, savedEntity.isLimitedAccount)
        assertEquals(0L, savedEntity.balance)
        assertNotNull(savedEntity.createdAt)
        assertNotNull(savedEntity.updatedAt)
    }

    @Test
    @DisplayName("계좌 저장이 호출되어야 한다")
    fun `계좌 저장 호출 테스트`() {
        // given
        `when`(generateAccountNumberUseCase.execute()).thenReturn(generatedAccountNumber)
        `when`(aes256Util.encrypt(testPassword)).thenReturn("encryptedPassword")
        `when`(createAccountEntityService.execute(any(), any(), any())).thenReturn(testAccountEntity)
        `when`(accountRepository.save(any(AccountEntity::class.java))).thenReturn(testAccountEntity)

        // when
        createAccountService.execute(testRequest)

        // then
        verify(accountRepository, times(1)).save(any(AccountEntity::class.java))
    }

    @Test
    @DisplayName("생성된 계좌번호가 321로 시작하는 13자리여야 한다")
    fun `계좌번호 321 형식 검증 테스트`() {
        // given
        `when`(generateAccountNumberUseCase.execute()).thenReturn(generatedAccountNumber)
        `when`(aes256Util.encrypt(testPassword)).thenReturn("encryptedPassword")
        `when`(createAccountEntityService.execute(any(), any(), any())).thenReturn(testAccountEntity)
        `when`(accountRepository.save(any(AccountEntity::class.java))).thenReturn(testAccountEntity)

        // when
        createAccountService.execute(testRequest)

        // then
        val captor = ArgumentCaptor.forClass(AccountEntity::class.java)
        verify(accountRepository).save(captor.capture())
        
        val savedEntity = captor.value
        assertEquals(13, savedEntity.accountNumber.length)
        assertTrue(savedEntity.accountNumber.all { it.isDigit() })
        assertTrue(savedEntity.accountNumber.startsWith("321"))
    }

    @Test
    @DisplayName("계좌번호가 숫자로만 구성되어야 한다")
    fun `계좌번호 숫자 검증 테스트`() {
        // given
        `when`(generateAccountNumberUseCase.execute()).thenReturn(generatedAccountNumber)
        `when`(aes256Util.encrypt(testPassword)).thenReturn("encryptedPassword")
        `when`(createAccountEntityService.execute(any(), any(), any())).thenReturn(testAccountEntity)
        `when`(accountRepository.save(any(AccountEntity::class.java))).thenReturn(testAccountEntity)

        // when
        createAccountService.execute(testRequest)

        // then
        val captor = ArgumentCaptor.forClass(AccountEntity::class.java)
        verify(accountRepository).save(captor.capture())
        
        val savedEntity = captor.value
        assertTrue(savedEntity.accountNumber.all { it.isDigit() })
    }



    @Test
    @DisplayName("13자리가 아닌 계좌번호는 예외를 발생시켜야 한다")
    fun `13자리가 아닌 계좌번호 예외 테스트`() {
        // given
        val invalidAccountNumber = "123456789012" // 12자리
        `when`(generateAccountNumberUseCase.execute()).thenReturn(invalidAccountNumber)

        // when & then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            createAccountService.execute(testRequest)
        }
        assertEquals("계좌번호는 13자리여야 합니다.", exception.message)
    }

    @Test
    @DisplayName("숫자가 아닌 문자가 포함된 계좌번호는 예외를 발생시켜야 한다")
    fun `숫자가 아닌 문자 포함 계좌번호 예외 테스트`() {
        // given
        val invalidAccountNumber = "321123456789A" // 문자 포함
        `when`(generateAccountNumberUseCase.execute()).thenReturn(invalidAccountNumber)

        // when & then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            createAccountService.execute(testRequest)
        }
        assertEquals("계좌번호는 숫자만 가능합니다.", exception.message)
    }

    @Test
    @DisplayName("321로 시작하지 않는 계좌번호는 예외를 발생시켜야 한다")
    fun `321로 시작하지 않는 계좌번호 예외 테스트`() {
        // given
        val invalidAccountNumber = "1231234567890" // 321로 시작하지 않음
        `when`(generateAccountNumberUseCase.execute()).thenReturn(invalidAccountNumber)

        // when & then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            createAccountService.execute(testRequest)
        }
        assertEquals("계좌번호는 321로 시작해야 합니다.", exception.message)
    }

    @Test
    @DisplayName("빈 문자열 계좌번호는 예외를 발생시켜야 한다")
    fun `빈 문자열 계좌번호 예외 테스트`() {
        // given
        val invalidAccountNumber = ""
        `when`(generateAccountNumberUseCase.execute()).thenReturn(invalidAccountNumber)

        // when & then
        val exception = assertThrows(IllegalArgumentException::class.java) {
            createAccountService.execute(testRequest)
        }
        assertEquals("계좌번호는 13자리여야 합니다.", exception.message)
    }




}