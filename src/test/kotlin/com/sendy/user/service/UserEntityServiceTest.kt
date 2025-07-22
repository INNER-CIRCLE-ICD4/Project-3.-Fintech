//package com.sendy.user.service
//
//import com.sendy.application.dto.RegisterUserRequestDto
//import com.sendy.user.domain.repository.UserRepository
//import org.junit.jupiter.api.Test
//
//import org.junit.jupiter.api.Assertions.*
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.boot.test.context.SpringBootTest
//
//@SpringBootTest
//class UserEntityServiceTest(
//    @Autowired private val userService: UserService
//    ,@Autowired private val userRepository: UserRepository
//) {
//
//
//
//    @Test
//    fun registerUser() {
//        // Given
//        val requestDto = RegisterUserRequestDto(
//            name = "이진경",
//            password = "1234",
//            phoneNumber = "01012345678",
//            email = "ljkg1208@gmail.com",
//            birth = "931208")
//
//       val user=  userService.registerUser(requestDto)
//
//        userRepository.findByUserId(user.userId)
//    }
//
//    @Test
//    fun updateUser() {
//    }
//
//    @Test
//    fun deleteUser() {
//    }
//
//    @Test
//    fun sendVerificationEmail() {
//    }
//
//    @Test
//    fun verifyEmail() {
//    }
//}