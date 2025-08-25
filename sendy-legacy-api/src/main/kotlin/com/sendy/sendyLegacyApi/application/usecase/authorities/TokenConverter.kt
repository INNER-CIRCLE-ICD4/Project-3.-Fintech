package com.sendy.sendyLegacyApi.application.usecase.authorities

import com.sendy.sendyLegacyApi.application.dto.authorities.TokenResponseDto
import com.sendy.sendyLegacyApi.application.dto.authorities.TokenDto
import com.sendy.sendyLegacyApi.support.error.ErrorCode
import com.sendy.sendyLegacyApi.support.exception.ServiceException
import org.springframework.stereotype.Component

@Component
class TokenConverter {
    fun tokenResponse(
        accessToken: TokenDto?,
        refreshToken: TokenDto?,
    ): TokenResponseDto {
        requireNotNull(accessToken) { throw ServiceException(ErrorCode.NULL_POINT) }
        requireNotNull(refreshToken) { throw ServiceException(ErrorCode.NULL_POINT) }

        return TokenResponseDto(
            accessToken = accessToken.token,
            accessTokenExpiredAt = accessToken.expiredAt,
            refreshToken = refreshToken.token,
            refreshTokenExpiredAt = refreshToken.expiredAt,
        )
    }
}
