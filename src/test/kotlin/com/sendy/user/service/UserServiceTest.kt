// package com.sendy.user.service
//
// import com.sendy.application.dto.CreateUserDto
// import com.sendy.application.dto.user.UpdateUserDto
// import com.sendy.domain.auth.UserRepository
// import com.sendy.domain.auth.token.service.TokenService
// import com.sendy.domain.service.UserService
// import org.junit.jupiter.api.Test
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.context.SpringBootTest
// import org.springframework.test.annotation.Rollback
//
// @SpringBootTest
//class UserServiceTest {
//
//    @Autowired
//    private lateinit var userService: UserService
//
//    @Autowired
//    private lateinit var userRepository: UserRepository
//
//    @Autowired
//    private lateinit var tokenService: TokenService
//
//    // 테스트용 사용자 생성 헬퍼 메서드
//    private fun createTestUser(email: String = "test@example.com"): Long {
//        val requestDto = CreateUserDto(
//            name = "테스트사용자",
//            password = "1234",
//            phoneNumber = "01012345678",
//            email = email,
//            birth = "19900101"
//        )
//        val user = userService.registerUser(requestDto)
//        return user.id
//    }
//
//    @Test
//    @Rollback
//    fun registerUser() {
//        // Given
//        val requestDto = CreateUserDto(
//            name = "이진경",
//            password = "1234",
//            phoneNumber = "01012345678",
//            email = "test@gmail.com",
//            birth = "19900101"
//        )
//
//        val user = userService.registerUser(requestDto)
//        println(user)
//    }
//
//    @Test
//    @Rollback
//    fun updateUser() {
//        // Given: 테스트용 사용자 생성
//        val userId = createTestUser("update-test@example.com")
//        val dto = UpdateUserDto(
//            name = "수정된이름",
//            password = "",
//            phoneNumber = "01011112222",
//            email = "",
//        )
//
//        // When: 사용자 정보 업데이트
//        userService.updateUser(userId, dto)
//
//        // Then: 업데이트 성공 확인
//        val updatedUser = userRepository.findById(userId).get()
//        println("Updated user: ${updatedUser.name}, ${updatedUser.phoneNumber}")
//    }
//
//
//    @Test
//    @Rollback
//    fun deleteUser() {
//        // Given: 테스트용 사용자 생성
//        val testEmail = "delete-test@example.com"
//        val userId = createTestUser(testEmail)
//
//        // When: 사용자 삭제
//        val result = userService.deleteUser(testEmail, "1234", userId)
//
//        // Then: 삭제 결과 확인
//        println("Delete user result: $result")
//    }
//
//    @Test
//    @Rollback
//    fun verifyEmail() {
//        // Given: 테스트용 사용자 생성
//        val testEmail = "verify-test@example.com"
//        createTestUser(testEmail)
//
//        // When: 이메일 인증 (예시 인증코드 사용)
//        val result = userService.verifyEmail("123123", testEmail)
//
//        // Then: 인증 결과 확인
//        println("Email verification result: $result")
//    }
//}
