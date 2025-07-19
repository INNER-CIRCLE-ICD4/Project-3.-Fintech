package com.sendy.security.auth.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.sendy.domain.token.helper.JwtTokenHelper
import com.sendy.domain.token.service.JwtTokenStorageService
import com.sendy.domain.service.CustomerUserDetailsService
import com.sendy.infrastructure.persistence.TokenStatus
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenHelper: JwtTokenHelper,
    private val jwtTokenStorageService: JwtTokenStorageService,
    private val userDetailsService: CustomerUserDetailsService
) : OncePerRequestFilter() {

    private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    private val objectMapper = ObjectMapper()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = getTokenFromRequest(request)
        
        if (token != null) {
            try {
                // JWT 자체 검증 (서명, 만료시간 등)
                val claims = jwtTokenHelper.validationTokenWithThrow(token)
                val userIdStr = claims["userId"] as? String
                val jti = claims["jti"] as? String
                
                // userId를 Long으로 변환
                val userId = userIdStr?.toLongOrNull()
                
                // 데이터베이스에서 토큰 상태 확인 (jti 사용)
                if (userId != null && jti != null) {
                    val tokenStatus = jwtTokenStorageService.getTokenStatus(jti)
                    
                    when (tokenStatus) {
                        TokenStatus.ACTIVE -> {
                            // 토큰이 유효한 경우 인증 설정
                            val userDetails = userDetailsService.loadUserByUsername(userId.toString())
                            val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                            SecurityContextHolder.getContext().authentication = authentication
                        }
                        TokenStatus.PENDING_LOGOUT -> {
                            // 다른 디바이스에서 로그인 시도 - 사용자 확인 필요
                            logger.warn("PENDING_LOGOUT 상태 토큰으로 접근 시도 - 사용자 ID: $userId")
                            sendPendingLogoutResponse(response)
                            return
                        }
                        TokenStatus.REVOKED -> {
                            // 토큰이 무효화된 경우
                            logger.warn("무효화된 토큰으로 접근 시도 - 사용자 ID: $userId")
                            sendRevokedTokenResponse(response)
                            return
                        }
                        null -> {
                            // 토큰이 존재하지 않거나 만료된 경우
                            logger.warn("존재하지 않거나 만료된 토큰 - 사용자 ID: $userId")
                            sendInvalidTokenResponse(response)
                            return
                        }
                    }
                } else {
                    // userId 또는 jti를 추출할 수 없는 경우
                    logger.warn("토큰에서 사용자 ID 또는 JTI를 추출할 수 없습니다. userId: $userIdStr, jti: $jti")
                    sendInvalidTokenResponse(response)
                    return
                }
            } catch (e: Exception) {
                // JWT 파싱 실패 또는 만료된 토큰
                logger.warn("토큰 검증 실패: ${e.message}")
                sendInvalidTokenResponse(response)
                return
            }
        }
        
        filterChain.doFilter(request, response)
    }

    private fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }
    
    /**
     * 다른 디바이스에서 로그인 시도 - 사용자 확인 필요
     */
    private fun sendPendingLogoutResponse(response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json;charset=UTF-8"
        
        val errorResponse = mapOf(
            "error" to "PENDING_LOGOUT",
            "message" to "다른 디바이스에서 로그인을 시도했습니다. 현재 디바이스에서 계속 사용하시겠습니까?",
            "code" to "DEVICE_CONFLICT",
            "actions" to mapOf(
                "continue" to "/api/auth/continue-session",
                "logout" to "/api/auth/confirm-logout"
            )
        )
        
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
    
    /**
     * 완전히 무효화된 토큰에 대한 응답
     */
    private fun sendRevokedTokenResponse(response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json;charset=UTF-8"
        
        val errorResponse = mapOf(
            "error" to "TOKEN_REVOKED",
            "message" to "토큰이 무효화되었습니다. 다시 로그인해주세요.",
            "code" to "TOKEN_REVOKED"
        )
        
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
    
    /**
     * 일반적인 토큰 오류 응답
     */
    private fun sendInvalidTokenResponse(response: HttpServletResponse) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/json;charset=UTF-8"
        
        val errorResponse = mapOf(
            "error" to "INVALID_TOKEN",
            "message" to "유효하지 않은 토큰입니다. 다시 로그인해주세요.",
            "code" to "TOKEN_INVALID"
        )
        
        response.writer.write(objectMapper.writeValueAsString(errorResponse))
    }
}