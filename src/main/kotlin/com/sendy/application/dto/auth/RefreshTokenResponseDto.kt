package com.sendy.application.dto.auth

import java.time.LocalDateTime

data class RefreshTokenResponseDto(
    val accessToken: String,
    val accessTokenExpiredAt: LocalDateTime,
) 
