package com.sendy.sendyLegacyApi.domain.auth.token.converter

import com.sendy.sendyLegacyApi.domain.auth.token.controller.model.TokenResponse
import com.sendy.sendyLegacyApi.domain.auth.token.model.TokenDto
import com.sendy.sendyLegacyApi.support.error.ErrorCode
import com.sendy.sendyLegacyApi.support.exception.ServiceException
import org.springframework.stereotype.Component

@Component
class TokenConverter {
    fun tokenResponse(
        accessToken: TokenDto?,
        refreshToken: TokenDto?,
    ): TokenResponse {
        requireNotNull(accessToken) { throw ServiceException(ErrorCode.NULL_POINT) }
        requireNotNull(refreshToken) { throw ServiceException(ErrorCode.NULL_POINT) }

        return TokenResponse(
            accessToken = accessToken.token,
            accessTokenExpiredAt = accessToken.expiredAt,
            refreshToken = refreshToken.token,
            refreshTokenExpiredAt = refreshToken.expiredAt,
        )
    }
}
