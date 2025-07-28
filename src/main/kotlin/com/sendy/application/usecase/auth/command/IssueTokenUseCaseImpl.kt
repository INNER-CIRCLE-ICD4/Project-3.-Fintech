package com.sendy.application.usecase.auth.command

import com.sendy.application.dto.auth.DeviceInfoDto
import com.sendy.application.usecase.auth.DeviceService
import com.sendy.application.usecase.auth.interfaces.IssueTokenUseCase
import com.sendy.domain.auth.token.controller.model.TokenResponse
import com.sendy.domain.auth.token.service.TokenService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class IssueTokenUseCaseImpl(
    private val deviceService: DeviceService,
    private val tokenService: TokenService,
) : IssueTokenUseCase {
    
    private val logger = LoggerFactory.getLogger(IssueTokenUseCaseImpl::class.java)
    
    override fun execute(userId: Long, deviceInfo: DeviceInfoDto, request: HttpServletRequest): TokenResponse {
        // 새 디바이스 정보 저장/업데이트
        val device = deviceService.saveOrUpdateDevice(userId, deviceInfo, request)

        // 새 토큰 발급 (디바이스 ID 포함)
        logger.info("사용자 ID ${userId}, 디바이스 ID ${device.id}에 새 토큰을 발급합니다")
        return tokenService.issueToken(userId, device.id)
    }
} 