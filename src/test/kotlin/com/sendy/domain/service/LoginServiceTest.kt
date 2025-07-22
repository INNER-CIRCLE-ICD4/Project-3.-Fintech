// package com.sendy.domain.service
//
// import com.sendy.support.util.SHA256Util
// import com.common.domain.error.ErrorCode
// import com.common.domain.exceptions.ApiException
// import com.sendy.application.dto.LoginRequestDto
// import com.sendy.domain.model.User
// import com.sendy.domain.repository.UserRepository
// import com.sendy.domain.token.controller.model.TokenResponse
// import com.sendy.domain.token.service.TokenService
// import com.sendy.domain.token.service.JwtTokenStorageService
// import com.sendy.domain.service.DeviceService
// import io.mockk.every
// import io.mockk.mockk
// import io.mockk.verify
// import org.junit.jupiter.api.Assertions.*
// import org.junit.jupiter.api.BeforeEach
// import org.junit.jupiter.api.DisplayName
// import org.junit.jupiter.api.Test
// import java.time.LocalDateTime
// import java.util.*
//
// class LoginServiceTest {
//
//    private lateinit var loginService: LoginService
//    private lateinit var userRepository: UserRepository
//    private lateinit var sha256Util: SHA256Util
//    private lateinit var tokenService: TokenService
//    private lateinit var deviceService: DeviceService
//    private lateinit var jwtTokenStorageService: JwtTokenStorageService
//
//    @BeforeEach
//    fun setUp() {
//        userRepository = mockk()
//        sha256Util = mockk()
//        tokenService = mockk()
//        deviceService = mockk()
//        jwtTokenStorageService = mockk()
//        loginService = LoginService(userRepository, sha256Util, tokenService, deviceService, jwtTokenStorageService)
//    }
//
//    @Test
//    @DisplayName("올바른 사용자 정보로 로그인하면 토큰이 발급된다")
//    fun login_ShouldReturnTokenResponse_WhenValidCredentials() {
//        // Given
//        val loginRequest = LoginRequestDto(id = 1L, password = "password123", deviceInfo = null)
//        val user = User(
//            id = 1L,
//            email = "test@test.com",
//            password = "hashed_password",
//            name = "테스트사용자",
//            phoneNumber = "010-1234-5678",
//            emailVerified = true
//        )
//        val expectedTokenResponse = TokenResponse(
//            accessToken = "access_token_123",
//            accessTokenExpiredAt = LocalDateTime.now().plusHours(1),
//            refreshToken = "refresh_token_456",
//            refreshTokenExpiredAt = LocalDateTime.now().plusHours(24)
//        )
//
//        every { userRepository.findActiveById(loginRequest.id) } returns Optional.of(user)
//        every { sha256Util.hash(loginRequest.password) } returns "hashed_password"
//        every { jwtTokenStorageService.setPendingLogoutByUserId(user.id) } returns Unit
//        every { deviceService.saveOrUpdateDevice(user.id, loginRequest.deviceInfo, any()) } returns mockk()
//        every { userRepository.save(any()) } returns user
//        every { tokenService.issueToken(user.id, any()) } returns expectedTokenResponse
//
//        // When
//        val result = loginService.login(loginRequest, mockk())
//
//        // Then
//        assertNotNull(result)
//        assertEquals(expectedTokenResponse.accessToken, result.accessToken)
//        assertEquals(expectedTokenResponse.refreshToken, result.refreshToken)
//        assertNotNull(result.accessTokenExpiredAt)
//        assertNotNull(result.refreshTokenExpiredAt)
//
//        verify(exactly = 1) { userRepository.findActiveById(loginRequest.id) }
//        verify(exactly = 1) { sha256Util.hash(loginRequest.password) }
//        verify(exactly = 1) { jwtTokenStorageService.setPendingLogoutByUserId(user.id) }
//        verify(exactly = 1) { tokenService.issueToken(user.id, any()) }
//    }
//
//    @Test
//    @DisplayName("존재하지 않는 사용자 ID로 로그인하면 예외가 발생한다")
//    fun login_ShouldThrowException_WhenUserNotFound() {
//        // Given
//        val loginRequest = LoginRequestDto(id = 999L, password = "password123", deviceInfo = null)
//
//        every { userRepository.findActiveById(loginRequest.id) } returns Optional.empty()
//
//        // When & Then
//        val exception = assertThrows(ApiException::class.java) {
//            loginService.login(loginRequest, mockk())
//        }
//
//        assertEquals(ErrorCode.NOT_FOUND, exception.getErrorCodeIfs())
//        verify(exactly = 1) { userRepository.findActiveById(loginRequest.id) }
//        verify(exactly = 0) { sha256Util.hash(any()) }
//        verify(exactly = 0) { tokenService.issueToken(any(), any()) }
//    }
//
//    @Test
//    @DisplayName("이메일 인증이 완료되지 않은 사용자는 로그인할 수 없다")
//    fun login_ShouldThrowException_WhenEmailNotVerified() {
//        // Given
//        val loginRequest = LoginRequestDto(id = 1L, password = "password123", deviceInfo = null)
//        val user = User(
//            id = 1L,
//            email = "test@test.com",
//            password = "hashed_password",
//            name = "테스트사용자",
//            phoneNumber = "010-1234-5678",
//            emailVerified = false // 이메일 인증 미완료
//        )
//
//        every { userRepository.findActiveById(loginRequest.id) } returns Optional.of(user)
//
//        // When & Then
//        val exception = assertThrows(ApiException::class.java) {
//            loginService.login(loginRequest, mockk())
//        }
//
//        assertEquals(ErrorCode.INVALID_INPUT_VALUE, exception.getErrorCodeIfs())
//        verify(exactly = 1) { userRepository.findActiveById(loginRequest.id) }
//        verify(exactly = 0) { sha256Util.hash(any()) }
//        verify(exactly = 0) { tokenService.issueToken(any(), any()) }
//    }
//
//    @Test
//    @DisplayName("잘못된 비밀번호로 로그인하면 예외가 발생한다")
//    fun login_ShouldThrowException_WhenWrongPassword() {
//        // Given
//        val loginRequest = LoginRequestDto(id = 1L, password = "wrong_password", deviceInfo = null)
//        val user = User(
//            id = 1L,
//            email = "test@test.com",
//            password = "hashed_password",
//            name = "테스트사용자",
//            phoneNumber = "010-1234-5678",
//            emailVerified = true
//        )
//
//        every { userRepository.findActiveById(loginRequest.id) } returns Optional.of(user)
//        every { sha256Util.hash(loginRequest.password) } returns "wrong_hashed_password"
//
//        // When & Then
//        val exception = assertThrows(ApiException::class.java) {
//            loginService.login(loginRequest, mockk())
//        }
//
//        assertEquals(ErrorCode.INVALID_INPUT_VALUE, exception.getErrorCodeIfs())
//        verify(exactly = 1) { userRepository.findActiveById(loginRequest.id) }
//        verify(exactly = 1) { sha256Util.hash(loginRequest.password) }
//        verify(exactly = 0) { tokenService.issueToken(any(), any()) }
//    }
// }
