package com.sendy.application.usecase.auth.command

import com.sendy.application.usecase.auth.interfaces.VerifyUserCredentials
import com.sendy.domain.auth.UserRepository
import com.sendy.domain.model.User
import com.sendy.support.error.ErrorCode
import com.sendy.support.exception.ServiceException
import com.sendy.support.util.SHA256Util
import org.springframework.stereotype.Service

@Service
class VerifyUserCredentialsImpl(
    private val userRepository: UserRepository,
    private val sha256Util: SHA256Util,
) : VerifyUserCredentials {
    override fun execute(
        userId: String,
        password: String,
    ): User {
        // 사용자 인증 (도메인 모델 사용)
        val user =
            userRepository
                .findActiveById(userId.toLong())
                .orElseThrow { ServiceException(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다") }

        // 도메인 로직을 통한 로그인 가능 여부 확인
        if (!user.canLogin()) {
            throw ServiceException(ErrorCode.INVALID_INPUT_VALUE, "로그인할 수 없는 사용자입니다")
        }

        // SHA-256 해시로 비밀번호 검증
        val hashedPassword = sha256Util.hash(password)
        if (!user.validatePassword(hashedPassword)) {
            throw ServiceException(ErrorCode.INVALID_INPUT_VALUE, "비밀번호가 일치하지 않습니다")
        }

        return user
    }
} 
