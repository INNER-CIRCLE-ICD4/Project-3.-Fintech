package com.sendy.application.dto.auth

data class LoginRequestDto(
    val id: Long,
    val password: String,
    val deviceInfo: DeviceInfoDto? = null,
)
