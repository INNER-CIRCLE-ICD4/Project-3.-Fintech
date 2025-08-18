package com.sendy.sendyLegacyApi.domain.auth.token.service

import com.sendy.sendyLegacyApi.application.dto.auth.RefreshTokenResponseDto
import com.sendy.sendyLegacyApi.domain.auth.token.business.TokenBusiness
import com.sendy.sendyLegacyApi.domain.auth.token.controller.model.TokenResponse
import com.sendy.sendyLegacyApi.domain.auth.token.converter.TokenConverter
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val tokenBusiness: TokenBusiness,
    private val tokenConverter: TokenConverter,
) {
    /**
     * 사용자 ID로 새로운 Access Token과 Refresh Token을 발급합니다.
     */
    fun issueToken(userId: Long): TokenResponse {
        val accessToken = tokenBusiness.issueAccessToken(userId)
        val refreshToken = tokenBusiness.issueRefreshToken(userId)

        return tokenConverter.tokenResponse(accessToken, refreshToken)
    }

    /**
     * 사용자 ID와 디바이스 ID로 새로운 Access Token과 Refresh Token을 발급합니다.
     */
    fun issueToken(
        userId: Long,
        deviceId: Long,
    ): TokenResponse {
        val accessToken = tokenBusiness.issueAccessToken(userId, deviceId)
        val refreshToken = tokenBusiness.issueRefreshToken(userId, deviceId)

        return tokenConverter.tokenResponse(accessToken, refreshToken)
    }

    /**
     * Refresh Token을 사용해서 새로운 Access Token을 발급합니다.
     * @param refreshToken 유효한 refresh token
     * @return 새로운 Access Token 정보
     */
    fun refreshAccessToken(refreshToken: String): RefreshTokenResponseDto {
        val newAccessToken = tokenBusiness.refreshAccessToken(refreshToken)

        return RefreshTokenResponseDto(
            accessToken = newAccessToken.token,
            accessTokenExpiredAt = newAccessToken.expiredAt,
        )
    }

    /**
     * 토큰을 검증하고 사용자 ID를 반환합니다.
     */
    fun validationToken(token: String): Long = tokenBusiness.validationToken(token)
}
