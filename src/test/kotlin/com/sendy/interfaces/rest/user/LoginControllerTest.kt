package com.sendy.interfaces.rest.user

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import com.sendy.application.dto.auth.DeviceInfoDto
import com.sendy.application.dto.auth.LoginRequestDto
import com.sendy.application.usecase.auth.LoginService
import com.sendy.application.usecase.auth.interfaces.LoginCommand
import com.sendy.application.usecase.auth.interfaces.LoginResult
import com.sendy.domain.auth.token.controller.model.TokenResponse
import io.mockk.every
import io.mockk.MockKAnnotations
import io.mockk.mockk
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDateTime

@WebMvcTest(
    controllers = [LoginController::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.REGEX,
            pattern = ["com.sendy.security.*"],
        ),
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [com.sendy.interfaces.filter.JwtAuthenticationFilter::class],
        ),
    ],
)
@TestPropertySource(
    properties = [
        "spring.main.allow-bean-definition-overriding=true",
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
        "jwt.secret-key=test-secret-key-for-testing-purposes-only",
        "jwt.access-token-expire-time=1",
        "jwt.refresh-token-expire-time=24",
    ],
)
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var loginService: LoginService
    
    private lateinit var mockRequest: HttpServletRequest
    
    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        mockRequest = mockk<HttpServletRequest>()
    }

    @Test
    @DisplayName("올바른 로그인 요청으로 토큰을 발급받는다")
    fun login_ShouldReturnTokenResponse_WhenValidRequest() {
        // Given
        val loginRequestDto = LoginRequestDto(
            id = 1L,
            email = "xxx@gmail.com", 
            password = "password123",
            deviceInfo = DeviceInfoDto(
                deviceName = "iPhone",
                userAgent = "Mozilla/5.0",
                ipAddress = "192.168.1.1",
                isMobile = true
            )
        )
        
        val tokenResponse = TokenResponse(
            accessToken = "access_token_123",
            accessTokenExpiredAt = LocalDateTime.now().plusHours(1),
            refreshToken = "refresh_token_456",
            refreshTokenExpiredAt = LocalDateTime.now().plusHours(24),
        )
        
        val loginResult = LoginResult(tokenResponse)

        every {
            loginService.login(any())
        } returns loginResult

        // When & Then
        mockMvc
            .perform(
                post("/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequestDto)),
            ).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.result.resultCode").value(200))
            .andExpect(jsonPath("$.result.resultMessage").value("OK"))
            .andExpect(jsonPath("$.body.tokenResponse.accessToken").value(tokenResponse.accessToken))
            .andExpect(jsonPath("$.body.tokenResponse.refreshToken").value(tokenResponse.refreshToken))
            .andExpect(jsonPath("$.body.tokenResponse.accessTokenExpiredAt").exists())
            .andExpect(jsonPath("$.body.tokenResponse.refreshTokenExpiredAt").exists())
    }

    @Test
    @DisplayName("잘못된 요청 형식으로 로그인하면 400 에러가 발생한다")
    fun login_ShouldReturnBadRequest_WhenInvalidRequestFormat() {
        // Given
        val invalidRequest = """{"invalidField": "value"}"""

        // When & Then
        mockMvc
            .perform(
                post("/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidRequest),
            ).andExpect(status().isBadRequest)
    }

    @Test
    @DisplayName("빈 요청 바디로 로그인하면 400 에러가 발생한다")
    fun login_ShouldReturnBadRequest_WhenEmptyRequestBody() {
        // When & Then
        mockMvc
            .perform(
                post("/users/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{}"),
            ).andExpect(status().isBadRequest)
    }
}
