package com.sendy.domain.service

import com.common.crypto.SHA256Util
import com.common.domain.error.ErrorCode
import com.common.domain.exceptions.ApiException
import com.sendy.application.dto.LoginRequestDto
import com.sendy.domain.repository.UserEntityRepository
import com.sendy.domain.token.controller.model.TokenResponse
import com.sendy.domain.token.service.TokenService
import com.sendy.domain.token.service.JwtTokenStorageService
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userEntityRepository: UserEntityRepository,
    private val sha256Util: SHA256Util,
    private val tokenService: TokenService,
    private val deviceService: DeviceService,
    private val jwtTokenStorageService: JwtTokenStorageService
) {
    
    private val logger = LoggerFactory.getLogger(LoginService::class.java)
    
    fun login(dto: LoginRequestDto, request: HttpServletRequest): TokenResponse {
        // 사용자 인증
        val user = userEntityRepository.findById(dto.id)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND) }
        
        // SHA-256 해시로 비밀번호 검증
        val hashedPassword = sha256Util.hash(dto.password)
        if (hashedPassword != user.password) {
            throw ApiException(ErrorCode.INVALID_INPUT_VALUE)
        }
        
        // ⚠️ 단일 디바이스 로그인 정책: 기존 모든 토큰을 PENDING_LOGOUT 상태로 변경
        logger.info("사용자 ID ${user.id}의 기존 모든 토큰을 PENDING_LOGOUT 상태로 변경합니다")
        jwtTokenStorageService.setPendingLogoutByUserId(user.id)
        
        // 새 디바이스 정보 저장/업데이트
        val device = deviceService.saveOrUpdateDevice(user.id, dto.deviceInfo, request)
        
        // 새 토큰 발급 (디바이스 ID 포함)
        logger.info("사용자 ID ${user.id}, 디바이스 ID ${device.id}에 새 토큰을 발급합니다")
        return tokenService.issueToken(user.id, device.id)
    }
}