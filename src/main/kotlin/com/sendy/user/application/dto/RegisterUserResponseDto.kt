package com.sendy.user.application.dto
import jakarta.validation.constraints.NotBlank
/**
 * 
 * User dto 클래스
 */
data class RegisterUserResponseDto(

    var userId: Long,

    var message: String,
)