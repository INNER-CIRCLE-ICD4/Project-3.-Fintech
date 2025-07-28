package com.sendy.interfaces.rest.auth

import com.sendy.domain.auth.token.service.JwtTokenStorageService
import com.sendy.domain.enum.TokenStatus
import com.sendy.interfaces.filter.JwtAuthenticationFilter
import com.sendy.support.response.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Auth Conflict", description = "디바이스 충돌 상황 처리 API")
@RestController
@RequestMapping("/authorities")
class AuthConflictController(
    private val jwtTokenStorageService: JwtTokenStorageService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    private val logger = LoggerFactory.getLogger(AuthConflictController::class.java)

    @Operation(
        summary = "현재 세션 계속 사용",
        description = """
            다른 디바이스에서 로그인 시도가 있었지만 현재 디바이스에서 계속 사용하겠다고 선택한 경우.
            현재 토큰을 ACTIVE 상태로 복원하고, 새로운 로그인은 무효화됩니다.
        """,
    )
    @PostMapping("/continue-session")
    fun continueSession(request: HttpServletRequest): Api<String> {
        val token = jwtAuthenticationFilter.getTokenFromRequest(request) ?: return Api.fail("토큰이 없습니다.")

        // JWT에서 jti 추출
        val jti = jwtAuthenticationFilter.getJtiFromToken(token) ?: return Api.fail("유효하지 않은 토큰입니다.")

        // 토큰 상태 확인 (jti 사용)
        val tokenStatus = jwtTokenStorageService.getTokenStatus(jti)

        if (tokenStatus != TokenStatus.PENDING_LOGOUT) {
            return Api.fail("PENDING_LOGOUT 상태가 아닙니다.")
        }

        // 사용자 ID 추출
        val userId = jwtAuthenticationFilter.getUserIdFromToken(token)
        if (userId == null) {
            return Api.fail("유효하지 않은 토큰입니다.")
        }

        // 현재 사용자의 PENDING_LOGOUT 토큰들을 ACTIVE로 복원
        jwtTokenStorageService.restorePendingTokensByUserId(userId)

        logger.info("사용자 ID $userId 의 세션을 계속 사용하도록 복원했습니다.")

        return Api.ok("현재 세션을 계속 사용합니다. 새로운 로그인은 차단되었습니다.")
    }

    @Operation(
        summary = "로그아웃 확인",
        description = """
            다른 디바이스에서의 로그인을 허용하고 현재 디바이스에서 로그아웃합니다.
            현재 토큰을 REVOKED 상태로 변경합니다. 
        """,
    )
    @PostMapping("/confirm-logout")
    fun confirmLogout(request: HttpServletRequest): Api<String> {
        val token = jwtAuthenticationFilter.getTokenFromRequest(request) ?: return Api.fail("토큰이 없습니다.")

        // JWT에서 jti 추출
        val jti = jwtAuthenticationFilter.getJtiFromToken(token) ?: return Api.fail("유효하지 않은 토큰입니다.")

        // 토큰 상태 확인 (jti 사용)
        val tokenStatus = jwtTokenStorageService.getTokenStatus(jti)

        if (tokenStatus != TokenStatus.PENDING_LOGOUT) {
            return Api.fail("PENDING_LOGOUT 상태가 아닙니다.")
        }

        // 사용자 ID 추출
        val userId = jwtAuthenticationFilter.getUserIdFromToken(token) ?: return Api.fail("유효하지 않은 토큰입니다.")

        // 현재 사용자의 모든 토큰을 REVOKED로 변경 (완전 로그아웃)
        jwtTokenStorageService.revokeAllTokensByUserId(userId)

        logger.info("사용자 ID $userId 가 로그아웃을 확인했습니다.")

        return Api.ok("로그아웃되었습니다. 새로운 디바이스에서 로그인이 허용됩니다.")
    }
}
