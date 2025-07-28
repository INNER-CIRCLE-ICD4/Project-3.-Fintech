package com.sendy.application.usecase.auth.command

import com.sendy.application.usecase.auth.interfaces.UpdateUserActivityUseCase
import com.sendy.domain.auth.UserRepository
import com.sendy.domain.model.User
import org.springframework.stereotype.Service

@Service
class UpdateUserActivityUseCaseImpl(
    private val userRepository: UserRepository,
) : UpdateUserActivityUseCase {
    
    override fun execute(user: User): User {
        // 사용자 마지막 활동 시간 업데이트
        return userRepository.save(user.updateLastActivity())
    }
} 