package com.sendy.application.usecase.auth.interfaces

import com.sendy.domain.model.User

interface AuthenticationUserUseCase {
    fun execute(email: String, password:String): User
}