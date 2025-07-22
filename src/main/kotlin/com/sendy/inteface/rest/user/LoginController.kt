package com.sendy.inteface.rest.user

import com.sendy.application.dto.LoginRequestDto
import com.sendy.domain.service.LoginService
import com.sendy.domain.token.controller.model.TokenResponse
import com.sendy.support.response.Response
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User", description = "사용자 인증 API")
@RestController
@RequestMapping("/user")
class LoginController(
    private val loginService: LoginService,
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
    ): Response<TokenResponse> {
        val tokenResponse = loginService.login(dto, request)
        return Response.ok(tokenResponse)
    }
}
