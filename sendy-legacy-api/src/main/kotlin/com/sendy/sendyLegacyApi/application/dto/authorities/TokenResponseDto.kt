package com.sendy.sendyLegacyApi.application.dto.authorities

import java.time.LocalDateTime

data class TokenResponseDto(
    val accessToken: String,
    val accessTokenExpiredAt: LocalDateTime,
    val refreshToken: String,
    val refreshTokenExpiredAt: LocalDateTime,
)
