package com.sendy.domain.token.converter

import com.sendy.domain.token.controller.model.TokenResponse
import com.sendy.domain.token.model.TokenDto
import com.sendy.support.error.ErrorCode
import com.sendy.support.exception.ApiException
import org.springframework.stereotype.Component

@Component
class TokenConverter {
    fun tokenResponse(
        accessToken: TokenDto?,
        refreshToken: TokenDto?,
    ): TokenResponse {
        requireNotNull(accessToken) { throw ApiException(ErrorCode.NULL_POINT) }
        requireNotNull(refreshToken) { throw ApiException(ErrorCode.NULL_POINT) }

        return TokenResponse(
            accessToken = accessToken.token,
            accessTokenExpiredAt = accessToken.expiredAt,
            refreshToken = refreshToken.token,
            refreshTokenExpiredAt = refreshToken.expiredAt,
        )
    }
}
