package com.sendy.application.usecase.auth.interfaces

import com.sendy.domain.user.UserEntity

interface UpdateUserActivity {
    fun execute(userEntity: UserEntity)
} 