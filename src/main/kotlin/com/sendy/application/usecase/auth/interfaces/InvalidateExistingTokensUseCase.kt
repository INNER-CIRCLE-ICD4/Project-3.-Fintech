package com.sendy.application.usecase.auth.interfaces

interface InvalidateExistingTokensUseCase {
    fun execute(userId: Long)
} 