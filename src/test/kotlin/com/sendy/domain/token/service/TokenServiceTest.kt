package com.sendy.domain.token.service

import com.sendy.application.dto.RefreshTokenResponseDto
import com.sendy.domain.token.business.TokenBusiness
import com.sendy.domain.token.controller.model.TokenResponse
import com.sendy.domain.token.converter.TokenConverter
import com.sendy.domain.token.model.TokenDto
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.verify
import java.time.LocalDateTime

class TokenServiceTest {

    private lateinit var tokenService: TokenService
    private lateinit var tokenBusiness: TokenBusiness
    private lateinit var tokenConverter: TokenConverter

    @BeforeEach
    fun setUp() {
        tokenBusiness = mockk()
        tokenConverter = mockk()
        tokenService = TokenService(tokenBusiness, tokenConverter)
    }

    @Test
    @DisplayName("사용자 ID로 토큰을 발급하면 AccessToken과 RefreshToken이 모두 생성된다")
    fun issueToken_ShouldReturnTokenResponse_WhenValidUserId() {
        // Given
        val userId = 1L
        val accessToken = TokenDto(
            token = "access_token_123",
            jti= "",
            expiredAt = LocalDateTime.now().plusHours(1)
        )
        val refreshToken = TokenDto(
            token = "refresh_token_456",
            jti= "",
            expiredAt = LocalDateTime.now().plusHours(24)
        )
        val expectedResponse = TokenResponse(
            accessToken = accessToken.token,
            accessTokenExpiredAt = accessToken.expiredAt,
            refreshToken = refreshToken.token,
            refreshTokenExpiredAt = refreshToken.expiredAt
        )

        every { tokenBusiness.issueAccessToken(userId) } returns accessToken
        every { tokenBusiness.issueRefreshToken(userId) } returns refreshToken
        every { tokenConverter.tokenResponse(accessToken, refreshToken) } returns expectedResponse

        // When
        val result = tokenService.issueToken(userId)

        // Then
        assertNotNull(result)
        assertEquals(expectedResponse.accessToken, result.accessToken)
        assertEquals(expectedResponse.refreshToken, result.refreshToken)
        assertEquals(expectedResponse.accessTokenExpiredAt, result.accessTokenExpiredAt)
        assertEquals(expectedResponse.refreshTokenExpiredAt, result.refreshTokenExpiredAt)

        verify(exactly = 1) { tokenBusiness.issueAccessToken(userId) }
        verify(exactly = 1) { tokenBusiness.issueRefreshToken(userId) }
        verify(exactly = 1) { tokenConverter.tokenResponse(accessToken, refreshToken) }
    }

    @Test
    @DisplayName("RefreshToken으로 새로운 AccessToken을 발급할 수 있다")
    fun refreshAccessToken_ShouldReturnNewAccessToken_WhenValidRefreshToken() {
        // Given
        val refreshToken = "valid_refresh_token"
        val newAccessToken = TokenDto(
            token = "new_access_token_789",
            jti= "",
            expiredAt = LocalDateTime.now().plusHours(1)
        )
        val expectedResponse = RefreshTokenResponseDto(
            accessToken = newAccessToken.token,
            accessTokenExpiredAt = newAccessToken.expiredAt
        )

        every { tokenBusiness.refreshAccessToken(refreshToken) } returns newAccessToken

        // When
        val result = tokenService.refreshAccessToken(refreshToken)

        // Then
        assertNotNull(result)
        assertEquals(expectedResponse.accessToken, result.accessToken)
        assertEquals(expectedResponse.accessTokenExpiredAt, result.accessTokenExpiredAt)

        verify(exactly = 1) { tokenBusiness.refreshAccessToken(refreshToken) }
    }

    @Test
    @DisplayName("토큰을 검증하고 사용자 ID를 반환한다")
    fun validationToken_ShouldReturnUserId_WhenValidToken() {
        // Given
        val token = "valid_token"
        val expectedUserId = 1L

        every { tokenBusiness.validationToken(token) } returns expectedUserId

        // When
        val result = tokenService.validationToken(token)

        // Then
        assertEquals(expectedUserId, result)
        verify(exactly = 1) { tokenBusiness.validationToken(token) }
    }
}