package com.sendy.domain.token.converter

import com.common.domain.error.ErrorCode
import com.common.domain.exceptions.ApiException
import com.sendy.domain.token.controller.model.TokenResponse
import com.sendy.domain.token.model.TokenDto
import org.springframework.stereotype.Component

@Component
class TokenConverter {
    
    fun tokenResponse(accessToken: TokenDto?, refreshToken: TokenDto?): TokenResponse {
        requireNotNull(accessToken) { throw ApiException(ErrorCode.NULL_POINT) }
        requireNotNull(refreshToken) { throw ApiException(ErrorCode.NULL_POINT) }

        return TokenResponse(
            accessToken = accessToken.token,
            accessTokenExpiredAt = accessToken.expiredAt,
            refreshToken = refreshToken.token,
            refreshTokenExpiredAt = refreshToken.expiredAt
        )
    }
}