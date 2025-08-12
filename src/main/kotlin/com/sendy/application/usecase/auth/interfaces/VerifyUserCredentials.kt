package com.sendy.application.usecase.auth.interfaces

import com.sendy.domain.user.UserEntity

interface VerifyUserCredentials {
    fun execute(userId: Long, password: String): UserEntity
} 