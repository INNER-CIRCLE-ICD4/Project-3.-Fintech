 feature/SENDY-5-62
 package com.sendy.user.service

 import com.sendy.application.dto.CreateUserDto
 import com.sendy.application.dto.user.UpdateUserDto
 import com.sendy.domain.auth.UserRepository
 import com.sendy.domain.auth.token.service.TokenService
 import com.sendy.domain.service.UserService
 import org.junit.jupiter.api.Test
 import org.springframework.beans.factory.annotation.Autowired
 import org.springframework.boot.test.context.SpringBootTest
 import org.springframework.test.annotation.Rollback

@SpringBootTest
class UserServiceTest {

    @Autowired 
    private lateinit var userService: UserService
    
    @Autowired 
    private lateinit var userRepository: UserRepository
    
    @Autowired 
    private lateinit var tokenService: TokenService


    @Test
    fun registerUser() {
        // Given
        val requestDto = CreateUserDto(
            name = "이진경",
            password = "1234",
            phoneNumber = "01012345678",
            email = "test@gmail.com",
            birth = "19900101"
        )

        val user = userService.registerUser(requestDto)
        println(user)
    }

    @Test
    fun updateUser() {
        val user = userRepository.findByEmail("test@gmail.com").get()
        val dto = UpdateUserDto(
            name = "",
            password = "",
            phoneNumber = "01011112222",
 feature/SENDY-5-62
            email = "",
            )

        val TokenResponse= tokenService.issueToken(user.id) // Assuming user ID 1 for testing

        userService.updateUser(user.id,dto)}


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
