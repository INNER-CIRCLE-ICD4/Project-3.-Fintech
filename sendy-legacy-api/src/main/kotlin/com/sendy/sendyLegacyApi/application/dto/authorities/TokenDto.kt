package com.sendy.sendyLegacyApi.application.dto.authorities

import java.time.LocalDateTime

data class TokenDto(
    val token: String,
    val jti: String, // JWT ID (고유 식별자)
    val expiredAt: LocalDateTime,
)
