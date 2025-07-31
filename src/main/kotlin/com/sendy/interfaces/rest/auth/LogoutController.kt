package com.sendy.interfaces.rest.user

import com.sendy.application.usecase.auth.LogoutService
import com.sendy.interfaces.filter.JwtAuthenticationFilter
import com.sendy.support.response.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/users")
class LogoutController(
    private val logoutService: LogoutService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,

    ) {
    @Operation(
        summary = "전체 로그아웃",
        description = """
            사용자의 모든 디바이스에서 로그아웃합니다.
            ⚠️ 클라이언트에서 반드시 수행해야 할 작업:
            1. 로컬스토리지/세션스토리지에서 토큰 삭제
            2. 쿠키에 저장된 토큰 삭제
            3. 메모리에 저장된 토큰 변수 초기화
            4. 인증이 필요한 페이지에서 로그인 페이지로 리다이렉트 
        """,
    )
    @PostMapping("/logout")
    fun logout(request: HttpServletRequest): Api<String> {
        val token = jwtAuthenticationFilter.getTokenFromRequest(request)

        if (token != null) {
            try {
                logoutService.logout(token)
            } catch (e: Exception) {
                // 토큰 검증 실패해도 로그아웃은 성공으로 처리
                // 보안상 구체적인 에러 정보는 노출하지 않음
            }
        }

        return Api.ok("모든 디바이스에서 로그아웃이 완료되었습니다. 클라이언트에서 토큰을 삭제해주세요.")
    }

    @Operation(
        summary = "현재 디바이스만 로그아웃",
        description = """
            현재 사용 중인 토큰만 무효화합니다. 다른 디바이스의 로그인 상태는 유지됩니다.
            ⚠️ 클라이언트에서 반드시 수행해야 할 작업:
            1. 로컬스토리지/세션스토리지에서 토큰 삭제
            2. 쿠키에 저장된 토큰 삭제
            3. 메모리에 저장된 토큰 변수 초기화
        """,
    )
    @PostMapping("/logout/current")
    fun logoutCurrent(request: HttpServletRequest): Api<String> {
        val token = jwtAuthenticationFilter.getTokenFromRequest(request)

        if (token != null) {
            try {
                logoutService.logoutCurrentDevice(token)
            } catch (e: Exception) {
                // 토큰 검증 실패해도 로그아웃은 성공으로 처리
                // 보안상 구체적인 에러 정보는 노출하지 않음
            }
        }

        return Api.ok("현재 디바이스에서 로그아웃이 완료되었습니다. 클라이언트에서 토큰을 삭제해주세요.")
    }
}
