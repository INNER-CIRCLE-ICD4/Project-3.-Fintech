package com.sendy.domain.model

import jakarta.annotation.Nonnull
import jakarta.validation.constraints.NotBlank


data class UserLoginRequest (
    @field:NotBlank
    var Id: Long = 0,
    @field:NotBlank
    var password: String = ""
)