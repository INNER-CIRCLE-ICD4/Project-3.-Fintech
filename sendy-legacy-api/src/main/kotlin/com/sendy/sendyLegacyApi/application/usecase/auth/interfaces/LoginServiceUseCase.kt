package com.sendy.sendyLegacyApi.application.usecase.auth.interfaces

import com.sendy.sendyLegacyApi.application.dto.auth.LoginRequestDto
import com.sendy.sendyLegacyApi.domain.auth.token.controller.model.TokenResponse
import jakarta.servlet.http.HttpServletRequest

interface LoginServiceUseCase {
    fun login(command: LoginCommand): LoginResult
}

data class LoginCommand(
    val dto: LoginRequestDto,
    val request: HttpServletRequest,
)

data class LoginResult(
    val tokenResponse: TokenResponse,
)
