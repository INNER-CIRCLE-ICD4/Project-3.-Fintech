package com.sendy.application.usecase.auth

import com.sendy.domain.auth.token.business.TokenBusiness
import com.sendy.domain.auth.token.ifs.TokenHelperIfs
import com.sendy.domain.auth.token.service.JwtTokenStorageService
import com.sendy.support.exception.ApiException
import org.springframework.stereotype.Service

@Service
class LogoutService(
    private val tokenBusiness: TokenBusiness,
    private val jwtTokenStorageService: JwtTokenStorageService,
    private val tokenHelperIfs: TokenHelperIfs,
) {
    /**
     * 전체 로그아웃 처리 - 사용자의 모든 디바이스에서 로그아웃
     */
    fun logout(token: String) {
        try {
            // 토큰에서 사용자 ID 추출
            val userId = tokenBusiness.validationToken(token)

            // 해당 사용자의 모든 토큰 무효화 (모든 디바이스)
            jwtTokenStorageService.revokeAllTokensByUserId(userId)
        } catch (e: ApiException) {
            // 토큰 검증 실패 시에도 JWT에서 jti를 추출해서 무효화 시도
            try {
                val claims = tokenHelperIfs.validationTokenWithThrow(token)
                val jti = claims["jti"]?.toString()
                if (jti != null) {
                    jwtTokenStorageService.revokeToken(jti)
                }
            } catch (ex: Exception) {
                // 토큰이 이미 무효하거나 만료된 경우에도 로그아웃 처리는 성공으로 간주
                // 보안상 구체적인 에러 정보는 노출하지 않음
            }
        }
    }

    /**
     * 현재 디바이스만 로그아웃 처리 - 특정 토큰만 무효화
     */
    fun logoutCurrentDevice(token: String) {
        try {
            // 토큰 유효성 검증 (사용자 ID 추출용)
            tokenBusiness.validationToken(token)

            // JWT에서 jti 추출하여 현재 토큰만 무효화
            val claims = tokenHelperIfs.validationTokenWithThrow(token)
            val jti = claims["jti"]?.toString()
            if (jti != null) {
                jwtTokenStorageService.revokeToken(jti)
            }
        } catch (e: ApiException) {
            // 토큰 검증 실패 시에도 JWT에서 jti를 추출해서 무효화 시도
            try {
                val claims = tokenHelperIfs.validationTokenWithThrow(token)
                val jti = claims["jti"]?.toString()
                if (jti != null) {
                    jwtTokenStorageService.revokeToken(jti)
                }
            } catch (ex: Exception) {
                // 토큰이 이미 무효하거나 만료된 경우에도 로그아웃 처리는 성공으로 간주
                // 보안상 구체적인 에러 정보는 노출하지 않음
            }
        }
    }
}
