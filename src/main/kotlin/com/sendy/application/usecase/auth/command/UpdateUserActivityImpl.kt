package com.sendy.application.usecase.auth.command

import com.sendy.application.usecase.auth.interfaces.UpdateUserActivity
import com.sendy.domain.auth.UserRepository
import com.sendy.domain.user.UserEntity
import org.springframework.stereotype.Service

@Service
class UpdateUserActivityImpl(
    private val userRepository: UserRepository,
) : UpdateUserActivity {

    override fun execute(userEntity: UserEntity) {
        // 사용자 마지막 활동 시간 업데이트
        userEntity.updateLastActivity()
    }
} 