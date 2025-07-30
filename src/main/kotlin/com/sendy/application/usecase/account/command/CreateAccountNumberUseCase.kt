package com.sendy.application.usecase.account.command

interface CreateAccountNumberUseCase {
    fun generate():String
}