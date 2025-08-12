package com.sendy.application.usecase.auth.command

import com.sendy.application.usecase.auth.interfaces.VerifyUserCredentials
import com.sendy.domain.auth.UserRepository
import com.sendy.domain.user.UserEntity
import com.sendy.support.error.ErrorCode
import com.sendy.support.exception.ServiceException
import com.sendy.support.util.SHA256Util
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import java.util.*

@ExtendWith(MockitoExtension::class)
class VerifyUserCredentialsTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var sha256Util: SHA256Util

    private lateinit var verifyUserCredentials: VerifyUserCredentials

    @BeforeEach
    fun setUp() {
        verifyUserCredentials = VerifyUserCredentialsImpl(userRepository, sha256Util)
    }

    @Test
    fun `정상적인 사용자 인증 테스트`() {
        // Given
        val userId = 1L
        val password = "password123"
        val hashedPassword = "hashedPassword123"

        val user =
            UserEntity(
                id = 1L,
                email = "test@example.com",
                password = hashedPassword,
                name = "테스트 사용자",
                phoneNumber = "01012345678",
                emailVerified = true,
                isDelete = false,
            )

        `when`(userRepository.findActiveById(1L)).thenReturn(Optional.of(user))
        `when`(sha256Util.hash(password)).thenReturn(hashedPassword)

        // When
        val result = verifyUserCredentials.execute(userId, password)

        // Then
        assertNotNull(result)
        assertEquals(user.id, result.id)
        assertEquals(user.email, result.email)
        assertEquals(user.name, result.name)
        assertTrue(result.canLogin())

        verify(userRepository).findActiveById(1L)
        verify(sha256Util).hash(password)
    }

    @Test
    fun `존재하지 않는 사용자 인증 실패 테스트`() {
        // Given
        val userId = 999L
        val password = "password123"

        `when`(userRepository.findActiveById(999L)).thenReturn(Optional.empty())

        // When & Then
        val exception =
            assertThrows(ServiceException::class.java) {
                verifyUserCredentials.execute(userId, password)
            }

        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCodeIfs())
        assertEquals("사용자를 찾을 수 없습니다", exception.message)

        verify(userRepository).findActiveById(999L)
        verify(sha256Util, never()).hash(any())
    }

    @Test
    fun `이메일 미인증 사용자 로그인 실패 테스트`() {
        // Given
        val userId = 1L
        val password = "password123"
        val hashedPassword = "hashedPassword123"

        val user =
            UserEntity(
                id = 1L,
                email = "test@example.com",
                password = hashedPassword,
                name = "테스트 사용자",
                phoneNumber = "01012345678",
                emailVerified = false, // 이메일 미인증
                isDelete = false,
            )

        `when`(userRepository.findActiveById(1L)).thenReturn(Optional.of(user))

        // When & Then
        val exception =
            assertThrows(ServiceException::class.java) {
                verifyUserCredentials.execute(userId, password)
            }

        assertEquals(ErrorCode.INVALID_INPUT_VALUE, exception.getErrorCodeIfs())
        assertEquals("로그인할 수 없는 사용자입니다", exception.message)

        verify(userRepository).findActiveById(1L)
        verify(sha256Util, never()).hash(any())
    }

    @Test
    fun `삭제된 사용자 로그인 실패 테스트`() {
        // Given
        val userId = 1L
        val password = "password123"

        val user =
            UserEntity(
                id = 1L,
                email = "test@example.com",
                password = "hashedPassword123",
                name = "테스트 사용자",
                phoneNumber = "01012345678",
                emailVerified = true,
                isDelete = true, // 삭제된 사용자
            )

        `when`(userRepository.findActiveById(1L)).thenReturn(Optional.of(user))

        // When & Then
        val exception =
            assertThrows(ServiceException::class.java) {
                verifyUserCredentials.execute(userId, password)
            }

        assertEquals(ErrorCode.INVALID_INPUT_VALUE, exception.getErrorCodeIfs())
        assertEquals("로그인할 수 없는 사용자입니다", exception.message)

        verify(userRepository).findActiveById(1L)
        verify(sha256Util, never()).hash(any())
    }

    @Test
    fun `잘못된 비밀번호 인증 실패 테스트`() {
        // Given
        val userId = 1L
        val password = "wrongPassword"
        val hashedPassword = "hashedPassword123"
        val wrongHashedPassword = "wrongHashedPassword"

        val user =
            UserEntity(
                id = 1L,
                email = "test@example.com",
                password = hashedPassword,
                name = "테스트 사용자",
                phoneNumber = "01012345678",
                emailVerified = true,
                isDelete = false,
            )

        `when`(userRepository.findActiveById(1L)).thenReturn(Optional.of(user))
        `when`(sha256Util.hash(password)).thenReturn(wrongHashedPassword)

        // When & Then
        val exception =
            assertThrows(ServiceException::class.java) {
                verifyUserCredentials.execute(userId, password)
            }

        assertEquals(ErrorCode.INVALID_INPUT_VALUE, exception.getErrorCodeIfs())
        assertEquals("비밀번호가 일치하지 않습니다", exception.message)

        verify(userRepository).findActiveById(1L)
        verify(sha256Util).hash(password)
    }
} 
