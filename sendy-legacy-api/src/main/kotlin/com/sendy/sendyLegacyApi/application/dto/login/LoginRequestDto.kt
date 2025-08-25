package com.sendy.sendyLegacyApi.application.dto.login

data class LoginRequestDto(
    val id: Long,
    val email: String,
    val password: String,
    val deviceInfo: DeviceInfoDto? = null,
)
