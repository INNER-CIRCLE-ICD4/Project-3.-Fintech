package com.sendy.application.dto

import jakarta.validation.constraints.NotBlank

data class RefreshTokenRequestDto(
    @field:NotBlank(message = "Refresh Token은 필수입니다")
    val refreshToken: String
) 