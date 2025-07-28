package com.sendy.application.usecase.auth.interfaces

import com.sendy.domain.model.User

interface AuthenticateUserUseCase {
    fun execute(userId: String, password: String): User
} 