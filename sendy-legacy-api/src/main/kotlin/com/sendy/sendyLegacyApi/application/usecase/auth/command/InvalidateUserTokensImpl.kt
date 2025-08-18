package com.sendy.sendyLegacyApi.application.usecase.auth.command

import com.sendy.sendyLegacyApi.application.usecase.auth.interfaces.InvalidateUserTokens
import com.sendy.sendyLegacyApi.domain.auth.token.service.JwtTokenStorageService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InvalidateUserTokensImpl(
    private val jwtTokenStorageService: JwtTokenStorageService,
) : InvalidateUserTokens {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun execute(userId: Long) {
        // ⚠️ 단일 디바이스 로그인 정책: 기존 모든 토큰을 PENDING_LOGOUT 상태로 변경
        logger.info("사용자 ID ${userId}의 기존 모든 토큰을 PENDING_LOGOUT 상태로 변경합니다")
        jwtTokenStorageService.setPendingLogoutByUserId(userId)
    }
} 
