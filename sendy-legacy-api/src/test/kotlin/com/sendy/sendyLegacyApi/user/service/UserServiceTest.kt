package com.sendy.sendyLegacyApi.user.service

import com.sendy.sendyLegacyApi.application.dto.users.CreateUserDto
import com.sendy.sendyLegacyApi.application.dto.users.UpdateUserDto
import com.sendy.sendyLegacyApi.application.usecase.users.UserService
import com.sendy.sendyLegacyApi.domain.authorities.UserEntityRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceTest(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserEntityRepository,
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
        val user = userRepository.findByEmail("test@gmail.com") ?: return
        val dto =
            UpdateUserDto(
                name = "",
                password = "",
                phoneNumber = "01011112222",
                email = "",
                birth = "",
            )
        userService.updateUser(user.id, dto)
    }

    @Test
    fun deleteUser() {
        val user = userRepository.findByEmail("test@gmail.com") ?: return
        userService.deleteUser("1234", user.id)

        println(userService.deleteUser("1234", user.id))
    }

//    @Test
//    fun verifyEmail() {
//        val result = userService.verifyEmail("123123", "test@gmail.com")
//        println(result)
//    }
}
