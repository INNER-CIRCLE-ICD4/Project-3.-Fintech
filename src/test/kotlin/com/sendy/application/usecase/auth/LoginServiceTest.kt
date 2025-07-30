package com.sendy.application.usecase.auth

import com.sendy.application.dto.auth.DeviceInfoDto
import com.sendy.application.dto.auth.LoginRequestDto
import com.sendy.application.usecase.auth.interfaces.*
import com.sendy.domain.auth.token.controller.model.TokenResponse
import com.sendy.domain.model.User
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class LoginServiceTest {

    @Mock
    private lateinit var verifyUserCredentials: VerifyUserCredentials

    @Mock
    private lateinit var invalidateUserTokens: InvalidateUserTokens

    @Mock
    private lateinit var updateUserActivity: UpdateUserActivity

    @Mock
    private lateinit var issueTokenUseCase: IssueTokenUseCase

    @Mock
    private lateinit var request: HttpServletRequest

    private lateinit var loginService: LoginService

    @BeforeEach
    fun setUp() {
        loginService = LoginService(
            verifyUserCredentials,
            invalidateUserTokens,
            updateUserActivity,
            issueTokenUseCase
        )
    }

    @Test
    fun `정상적인 로그인 플로우 테스트`() {
        // Given
        val loginRequestDto = LoginRequestDto(
            id = 1L,
            email = "test@example.com",
            password = "password123",
            deviceInfo = DeviceInfoDto(
                deviceName = "iPhone",
                userAgent = "Mozilla/5.0",
                ipAddress = "192.168.1.1",
                isMobile = true
            )
        )

        val command = LoginCommand(loginRequestDto, request)

        val user = User(
            id = 1L,
            email = "test@example.com",
            password = "hashedPassword",
            name = "테스트 사용자",
            phoneNumber = "01012345678",
            emailVerified = true,
            isDelete = false
        )

        val updatedUser = user.copy(updatedAt = user.updatedAt.plusHours(1))

        val tokenResponse = TokenResponse(
            accessToken = "access_token_123",
            refreshToken = "refresh_token_123",
            accessTokenExpiredAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59),
            refreshTokenExpiredAt = LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        )

        // Mock 설정
        `when`(verifyUserCredentials.execute(loginRequestDto.email, loginRequestDto.password))
            .thenReturn(user)
        `when`(updateUserActivity.execute(user)).thenReturn(updatedUser)
        `when`(issueTokenUseCase.execute(user.id, loginRequestDto.deviceInfo!!, request))
            .thenReturn(tokenResponse)

        // When
        val result = loginService.login(command)

        // Then
        assertNotNull(result)
        assertEquals(tokenResponse, result.tokenResponse)

        // 각 UseCase가 올바른 순서로 호출되었는지 확인
        verify(verifyUserCredentials).execute(loginRequestDto.email, loginRequestDto.password)
        verify(invalidateUserTokens).execute(user.id)
        verify(updateUserActivity).execute(user)
        verify(issueTokenUseCase).execute(user.id, loginRequestDto.deviceInfo!!, request)

        // 각 UseCase가 정확히 한 번씩만 호출되었는지 확인
        verifyNoMoreInteractions(verifyUserCredentials, invalidateUserTokens, updateUserActivity, issueTokenUseCase)
    }

    @Test
    fun `deviceInfo가 null인 경우 기본값으로 처리되는지 테스트`() {
        val loginRequestDto = LoginRequestDto(
            id = 1L,
            email = "test@example.com",
            password = "password123",
            deviceInfo = null
        )

        val command = LoginCommand(loginRequestDto, request)

        val user = User(
            id = 1L,
            email = "test@example.com",
            password = "hashedPassword",
            name = "테스트 사용자",
            phoneNumber = "01012345678",
            emailVerified = true,
            isDelete = false
        )

        val updatedUser = user.copy(updatedAt = user.updatedAt.plusHours(1))

        val tokenResponse = TokenResponse(
            accessToken = "access_token_123",
            refreshToken = "refresh_token_123",
            accessTokenExpiredAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59),
            refreshTokenExpiredAt = LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        )

        // Mockito-Kotlin 사용
        whenever(verifyUserCredentials.execute(loginRequestDto.email, loginRequestDto.password)).thenReturn(user)
        whenever(updateUserActivity.execute(user)).thenReturn(updatedUser)
        whenever(issueTokenUseCase.execute(eq(user.id), any(), eq(request)))
            .thenReturn(tokenResponse)

        val result = loginService.login(command)

        assertNotNull(result)
        assertEquals(tokenResponse, result.tokenResponse)

        verify(issueTokenUseCase).execute(eq(user.id), any(), eq(request))
    }



    @Test
    fun `인증 실패 시 다른 UseCase들이 호출되지 않는지 테스트`() {
        // Given
        val loginRequestDto = LoginRequestDto(
            id = 1L,
            email = "test@example.com",
            password = "wrongPassword",
            deviceInfo = DeviceInfoDto()
        )

        val command = LoginCommand(loginRequestDto, request)

        // 인증 실패 시나리오
        `when`(verifyUserCredentials.execute(loginRequestDto.email, loginRequestDto.password))
            .thenThrow(RuntimeException("인증 실패"))

        // When & Then
        assertThrows(RuntimeException::class.java) {
            loginService.login(command)
        }

        // 인증 UseCase만 호출되고 나머지는 호출되지 않았는지 확인
        verify(verifyUserCredentials).execute(loginRequestDto.email, loginRequestDto.password)
        verifyNoInteractions(invalidateUserTokens, updateUserActivity, issueTokenUseCase)
    }

    @Test
    fun `로그인 플로우의 순서가 올바른지 테스트`() {
        // Given
        val loginRequestDto = LoginRequestDto(
            id = 1L,
            email = "test@example.com",
            password = "password123",
            deviceInfo = DeviceInfoDto()
        )

        val command = LoginCommand(loginRequestDto, request)

        val user = User(
            id = 1L,
            email = "test@example.com",
            password = "hashedPassword",
            name = "테스트 사용자",
            phoneNumber = "01012345678",
            emailVerified = true,
            isDelete = false
        )

        val updatedUser = user.copy(updatedAt = user.updatedAt.plusHours(1))

        val tokenResponse = TokenResponse(
            accessToken = "access_token_123",
            refreshToken = "refresh_token_123",
            accessTokenExpiredAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59),
            refreshTokenExpiredAt = LocalDateTime.of(2025, 12, 31, 23, 59, 59)
        )

        // Mock 설정
        `when`(verifyUserCredentials.execute(any(), any())).thenReturn(user)
        `when`(updateUserActivity.execute(any())).thenReturn(updatedUser)
        `when`(issueTokenUseCase.execute(any(), any(), any())).thenReturn(tokenResponse)

        // When
        loginService.login(command)

        // Then - 호출 순서 확인
        val inOrder = inOrder(verifyUserCredentials, invalidateUserTokens, updateUserActivity, issueTokenUseCase)
        
        inOrder.verify(verifyUserCredentials).execute(loginRequestDto.email, loginRequestDto.password)
        inOrder.verify(invalidateUserTokens).execute(user.id)
        inOrder.verify(updateUserActivity).execute(user)
        inOrder.verify(issueTokenUseCase).execute(user.id, DeviceInfoDto(), request)
    }
} 