package com.sendy.sendyLegacyApi.application.usecase.auth.interfaces

interface InvalidateUserTokens {
    fun execute(userId: Long)
} 