package com.sendy.application.usecase.auth.interfaces

interface InvalidateUserTokens {
    fun execute(userId: Long)
} 