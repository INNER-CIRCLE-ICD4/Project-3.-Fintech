package com.sendy.sendyLegacyApi.application.dto.user
import io.swagger.v3.oas.annotations.media.Schema

/**
 *
 * User수정dto 클래스
 */
data class UpdateUserDto(

    val name: String?,

    val password : String?,

    @Schema(description = "핸드폰 번호", example = "01012345678")
    val phoneNumber: String?,

    @Schema(description = "이메일", example = "xxx@example.com")
    val email: String?
)