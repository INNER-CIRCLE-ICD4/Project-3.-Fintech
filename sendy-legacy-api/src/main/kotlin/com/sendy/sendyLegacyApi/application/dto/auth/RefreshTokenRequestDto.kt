package com.sendy.sendyLegacyApi.application.dto.auth

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequestDto(
    @field:NotBlank(message = "Refresh Token은 필수입니다")
    val refreshToken: String,
) 
