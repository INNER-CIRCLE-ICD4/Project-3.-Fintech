package com.sendy.sendyLegacyApi.user.service

import com.sendy.sendyLegacyApi.application.dto.user.CreateUserDto
import com.sendy.sendyLegacyApi.application.dto.user.UpdateUserDto
import com.sendy.sendyLegacyApi.application.usecase.user.UserService
import com.sendy.sendyLegacyApi.domain.auth.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository,
) {
    @Test
    fun registerUser() {
        // Given
        val requestDto =
            CreateUserDto(
                name = "이진경",
                password = "1234",
                phoneNumber = "01012345678",
                email = "test@gmail.com",
                birth = "19900101",
            )

        val user = userService.registerUser(requestDto)
        println(user)
    }

    @Test
    fun updateUser() {
        val user = userRepository.findByEmail("test@gmail.com").get()
        val dto =
            UpdateUserDto(
                name = "",
                password = "",
                phoneNumber = "01011112222",
                email = "",
            )
        userService.updateUser(user.id, dto)
    }

    @Test
    fun deleteUser() {
        val user = userRepository.findByEmail("test@gmail.com").get()
        userService.deleteUser("test@gmail.com", "1234", user.id)

        println(userService.deleteUser("test@gmail.com", "1234", user.id))
    }

    @Test
    fun verifyEmail() {
        val result = userService.verifyEmail("123123", "test@gmail.com")
        println(result)
    }
}
