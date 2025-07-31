package com.sendy.application.dto.user
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * User수정dto 클래스
 */
data class UpdateUserDto(

    var name: String?,

    var password : String?,

    @Schema(description = "핸드폰 번호", example = "01012345678")
    var phoneNumber: String?,

    @Schema(description = "이메일", example = "xxx@example.com")
    var email: String?
)