package com.sendy.sendyLegacyApi.application.usecase.auth.interfaces

import com.sendy.sendyLegacyApi.domain.user.UserEntity

interface VerifyUserCredentials {
    fun execute(
        userId: Long,
        password: String,
    ): UserEntity
} 
