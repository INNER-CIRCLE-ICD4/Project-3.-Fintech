package com.sendy.inteface.rest.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.sendy.domain.token.controller.model.TokenResponse
import com.ninjasquad.springmockk.MockkBean
import com.sendy.application.dto.LoginRequestDto
import com.sendy.domain.service.LoginService
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(
    controllers = [LoginController::class],
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.REGEX,
        pattern = ["com.sendy.security.*"]
    )]
)
@TestPropertySource(properties = [
    "spring.main.allow-bean-definition-overriding=true",
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
])
class LoginControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var loginService: LoginService

    @Test
    @DisplayName("올바른 로그인 요청으로 토큰을 발급받는다")
    fun login_ShouldReturnTokenResponse_WhenValidRequest() {
        // Given
        val loginRequest = LoginRequestDto(id = 1L, password = "password123")
        val tokenResponse = TokenResponse(
            accessToken = "access_token_123",
            accessTokenExpiredAt = LocalDateTime.now().plusHours(1),
            refreshToken = "refresh_token_456",
            refreshTokenExpiredAt = LocalDateTime.now().plusHours(24)
        )

        every { loginService.login(
            loginRequest,
            request = TODO()
        ) } returns tokenResponse

        // When & Then
        mockMvc.perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.message").value("OK"))
            .andExpect(jsonPath("$.value.accessToken").value(tokenResponse.accessToken))
            .andExpect(jsonPath("$.value.refreshToken").value(tokenResponse.refreshToken))
            .andExpect(jsonPath("$.value.accessTokenExpiredAt").exists())
            .andExpect(jsonPath("$.value.refreshTokenExpiredAt").exists())
    }

    @Test
    @DisplayName("잘못된 요청 형식으로 로그인하면 400 에러가 발생한다")
    fun login_ShouldReturnBadRequest_WhenInvalidRequestFormat() {
        // Given
        val invalidRequest = """{"invalidField": "value"}"""

        // When & Then
        mockMvc.perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("빈 요청 바디로 로그인하면 400 에러가 발생한다")
    fun login_ShouldReturnBadRequest_WhenEmptyRequestBody() {
        // When & Then
        mockMvc.perform(
            post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
            .andExpect(status().isBadRequest)
    }
}