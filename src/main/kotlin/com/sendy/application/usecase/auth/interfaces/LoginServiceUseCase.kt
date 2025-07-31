package com.sendy.application.usecase.auth.interfaces

import com.sendy.application.dto.auth.LoginRequestDto
import com.sendy.domain.auth.token.controller.model.TokenResponse
import jakarta.servlet.http.HttpServletRequest

interface LoginServiceUseCase {
    fun login(command: LoginCommand): LoginResult
}

data class LoginCommand(
    val dto : LoginRequestDto,
    val request : HttpServletRequest
)

data class LoginResult(
    val tokenResponse: TokenResponse
)