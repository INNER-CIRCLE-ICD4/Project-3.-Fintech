package com.sendy.sendyLegacyApi.application.usecase.auth.interfaces

import com.sendy.sendyLegacyApi.application.dto.auth.DeviceInfoDto
import com.sendy.sendyLegacyApi.domain.auth.token.controller.model.TokenResponse
import jakarta.servlet.http.HttpServletRequest

interface IssueTokenUseCase {
    fun execute(
        userId: Long,
        deviceInfo: DeviceInfoDto,
        request: HttpServletRequest,
    ): TokenResponse
} 
