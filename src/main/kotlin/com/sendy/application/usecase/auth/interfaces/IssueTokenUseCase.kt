package com.sendy.application.usecase.auth.interfaces

import com.sendy.application.dto.auth.DeviceInfoDto
import com.sendy.domain.auth.token.controller.model.TokenResponse
import jakarta.servlet.http.HttpServletRequest

interface IssueTokenUseCase {
    fun execute(userId: Long, deviceInfo: DeviceInfoDto, request: HttpServletRequest): TokenResponse
} 