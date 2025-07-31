package com.sendy.application.usecase.auth.interfaces

import com.sendy.domain.model.User

interface UpdateUserActivity {
    fun execute(user: User): User
} 