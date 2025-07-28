package com.sendy.interfaces.rest.auth

import com.sendy.application.dto.auth.RefreshTokenRequestDto
import com.sendy.application.dto.auth.RefreshTokenResponseDto
import com.sendy.domain.auth.token.service.TokenService
import com.sendy.support.response.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Token", description = "토큰 관리 API")
@RestController
@RequestMapping("/tokens")
class TokenController(
    private val tokenService: TokenService,
) {
    @Operation(
        summary = "Access Token 갱신",
        description =
            "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다. " +
                "Refresh Token이 만료된 경우 재로그인이 필요합니다.",
    )
    @PostMapping("/refresh")
    fun refreshToken(
        @Valid @RequestBody request: RefreshTokenRequestDto,
    ): Api<RefreshTokenResponseDto> {
        val refreshTokenResponse = tokenService.refreshAccessToken(request.refreshToken)
        return Api.ok(refreshTokenResponse)
    }
}
