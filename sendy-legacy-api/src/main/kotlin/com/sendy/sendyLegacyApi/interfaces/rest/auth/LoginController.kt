package com.sendy.sendyLegacyApi.interfaces.rest.auth

import com.sendy.sendyLegacyApi.application.dto.authorities.TokenResponseDto
import com.sendy.sendyLegacyApi.application.dto.login.LoginRequestDto
import com.sendy.sendyLegacyApi.application.usecase.authorities.LoginService
import com.sendy.sendyLegacyApi.application.usecase.authorities.LogoutService
import com.sendy.sendyLegacyApi.interfaces.filter.JwtAuthenticationFilter
import com.sendy.sendyLegacyApi.support.response.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Login", description = "로그인 API")
@RestController
@RequestMapping("/users/login")
class LoginController(
    private val loginService: LoginService,
    private val logoutService: LogoutService,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
) {
    @Operation(
        summary = "로그인",
        description = """
            사용자 인증 후 JWT 토큰을 발급합니다.
            디바이스 정보를 함께 전송하면 디바이스별 로그아웃 관리가 가능합니다.
        """,
    )
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody dto: LoginRequestDto,
        request: HttpServletRequest,
    ): Response<TokenResponseDto> {
        val result = loginService.login(dto, request)
        return Response.ok(result)
    }

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
    fun logout(request: HttpServletRequest): Response<String> {
        val token = jwtAuthenticationFilter.getTokenFromRequest(request)

        if (token != null) {
            try {
                logoutService.logout(token)
            } catch (e: Exception) {
                // 토큰 검증 실패해도 로그아웃은 성공으로 처리
                // 보안상 구체적인 에러 정보는 노출하지 않음
            }
        }

        return Response.ok("모든 디바이스에서 로그아웃이 완료되었습니다. 클라이언트에서 토큰을 삭제해주세요.")
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
    fun logoutCurrent(request: HttpServletRequest): Response<String> {
        val token = jwtAuthenticationFilter.getTokenFromRequest(request)

        if (token != null) {
            try {
                logoutService.logoutCurrentDevice(token)
            } catch (e: Exception) {
                // 토큰 검증 실패해도 로그아웃은 성공으로 처리
                // 보안상 구체적인 에러 정보는 노출하지 않음
            }
        }

        return Response.ok("현재 디바이스에서 로그아웃이 완료되었습니다. 클라이언트에서 토큰을 삭제해주세요.")
    }
}
