package com.sendy.sendyLegacyApi.application.usecase.auth.interfaces

import com.sendy.sendyLegacyApi.domain.user.UserEntity

interface UpdateUserActivity {
    fun execute(userEntity: UserEntity)
} 
