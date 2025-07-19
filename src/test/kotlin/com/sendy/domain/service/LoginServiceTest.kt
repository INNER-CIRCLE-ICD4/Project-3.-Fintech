//package com.sendy.domain.service
//
//import com.common.domain.error.ErrorCode
//import com.common.domain.exceptions.ApiException
//import com.sendy.application.dto.LoginRequestDto
//import com.sendy.domain.model.User
//import com.sendy.domain.token.controller.model.TokenResponse
//import com.sendy.domain.token.service.TokenService
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import org.junit.jupiter.api.Assertions.*
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.springframework.security.crypto.password.PasswordEncoder
//import java.time.LocalDateTime
//import java.util.*
//
//class LoginServiceTest {
//
//    private lateinit var loginService: LoginService
//    private lateinit var passwordEncoder: PasswordEncoder
//    private lateinit var tokenService: TokenService
//
//    @BeforeEach
//    fun setUp() {
//        userRepository = mockk()
//        passwordEncoder = mockk()
//        tokenService = mockk()
//        loginService = LoginService(userRepository, passwordEncoder, tokenService)
//    }
//
//    @Test
//    @DisplayName("올바른 사용자 정보로 로그인하면 토큰이 발급된다")
//    fun login_ShouldReturnTokenResponse_WhenValidCredentials() {
//        // Given
//        val loginRequest = LoginRequestDto(id = 1L, password = "password123")
//        val user = User(
//            id = 1L,
//            email = "test@test.com",
//            password = "encoded_password",
//            name = "테스트사용자"
//        )
//        val expectedTokenResponse = TokenResponse(
//            accessToken = "access_token_123",
//            accessTokenExpiredAt = LocalDateTime.now().plusHours(1),
//            refreshToken = "refresh_token_456",
//            refreshTokenExpiredAt = LocalDateTime.now().plusHours(24)
//        )
//
//        every { userRepository.findById(loginRequest.id) } returns Optional.of(user)
//        every { passwordEncoder.matches(loginRequest.password, user.password) } returns true
//        every { tokenService.issueToken(user.id) } returns expectedTokenResponse
//
//        // When
//        val result = loginService.login(loginRequest)
//
//        // Then
//        assertNotNull(result)
//        assertEquals(expectedTokenResponse.accessToken, result.accessToken)
//        assertEquals(expectedTokenResponse.refreshToken, result.refreshToken)
//        assertNotNull(result.accessTokenExpiredAt)
//        assertNotNull(result.refreshTokenExpiredAt)
//
//        verify(exactly = 1) { userRepository.findById(loginRequest.id) }
//        verify(exactly = 1) { passwordEncoder.matches(loginRequest.password, user.password) }
//        verify(exactly = 1) { tokenService.issueToken(user.id) }
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 사용자 ID로 로그인하면 예외가 발생한다")
//    fun login_ShouldThrowException_WhenUserNotFound() {
//        // Given
//        val loginRequest = LoginRequestDto(id = 999L, password = "password123")
//
//        every { userRepository.findById(loginRequest.id) } returns Optional.empty()
//
//        // When & Then
//        val exception = assertThrows(ApiException::class.java) {
//            loginService.login(loginRequest)
//        }
//
//        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCodeIfs())
//        verify(exactly = 1) { userRepository.findById(loginRequest.id) }
//        verify(exactly = 0) { passwordEncoder.matches(any(), any()) }
//        verify(exactly = 0) { tokenService.issueToken(any()) }
//    }
//
//    @Test
//    @DisplayName("잘못된 비밀번호로 로그인하면 예외가 발생한다")
//    fun login_ShouldThrowException_WhenWrongPassword() {
//        // Given
//        val loginRequest = LoginRequestDto(id = 1L, password = "wrong_password")
//        val user = User(
//            id = 1L,
//            email = "test@test.com",
//            password = "encoded_password",
//            name = "테스트사용자"
//        )
//
//        every { userRepository.findById(loginRequest.id) } returns Optional.of(user)
//        every { passwordEncoder.matches(loginRequest.password, user.password) } returns false
//
//        // When & Then
//        val exception = assertThrows(ApiException::class.java) {
//            loginService.login(loginRequest)
//        }
//
//        assertEquals(ErrorCode.INVALID_INPUT_VALUE, exception.getErrorCodeIfs())
//        verify(exactly = 1) { userRepository.findById(loginRequest.id) }
//        verify(exactly = 1) { passwordEncoder.matches(loginRequest.password, user.password) }
//        verify(exactly = 0) { tokenService.issueToken(any()) }
//    }
//}