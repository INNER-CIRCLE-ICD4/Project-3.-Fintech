package com.sendy.inteface.rest.user

import com.sendy.application.dto.LoginRequestDto
import com.sendy.application.dto.UserAccessTokenResponseDto
import com.sendy.domain.service.LoginService
import com.sendy.inteface.rest.user.ui.Response
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/login")
class LoginController(
    private val loginService: LoginService
) {
    @PostMapping
    fun login(@RequestBody dto: LoginRequestDto): Response<UserAccessTokenResponseDto> {
        val token = loginService.login(dto)
        return Response.ok(UserAccessTokenResponseDto(token))
    }


}