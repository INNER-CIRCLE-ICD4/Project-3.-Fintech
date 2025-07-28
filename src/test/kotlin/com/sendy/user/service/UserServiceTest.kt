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
 class UserServiceTest(
    @Autowired private val userService: UserService
    ,@Autowired private val userRepository: UserRepository
    ,@Autowired private val tokenService: TokenService
 ) {

    @Test
    @Rollback(false)
    fun registerUser() {
        // Given
        val requestDto = CreateUserDto(
            name = "이진경",
            password = "1234",
            phoneNumber = "01012345678",
            email = "test@gmail.com",
            birth = "931208")

       val user= userService.registerUser(requestDto)
        println(user)
    }

    @Test
    fun updateUser() {
       val user = userRepository.findByEmail("ljkg1208@gmail.com").get()
        val dto = UpdateUserDto(
            name = "",
            password = "",
            phoneNumber = "01011112222",
            email = "",
            birth = "900101")

        val TokenResponse= tokenService.issueToken(user.id) // Assuming user ID 1 for testing

        userService.updateUser(user.id,dto)}

    @Test
    fun deleteUser() {
        val user = userRepository.findByEmail("ljkg1208@gmail.com").get()
        userService.deleteUser("ljkg1208@gmail.com","1234",user.id)

        println(userService.deleteUser("ljkg1208@gmail.com","1234",user.id))
    }




    @Test
    fun verifyEmail() {
        val result = userService.verifyEmail("123123","test@gmail.com")
        println(result)
    }
 }
