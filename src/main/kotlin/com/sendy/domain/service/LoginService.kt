package com.sendy.domain.service

import com.common.domain.error.ErrorCode
import com.common.domain.exceptions.CustomException
import com.sendy.application.dto.LoginRequestDto
import com.sendy.domain.repository.UserRepository
import com.sendy.security.auth.common.JwtTokenProvider
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {
    fun login(dto: LoginRequestDto): String {
        val user = userRepository.findByEmail(dto.email)
            ?: throw CustomException(ErrorCode.NOT_FOUND)
        if (!passwordEncoder.matches(dto.password, user.password)) {
            throw CustomException(ErrorCode.INVALID_INPUT_VALUE)
        }
        return jwtTokenProvider.createToken(user.id, user.role)
    }
}