package com.sendy.application.dto
import com.sendy.infrastructure.persistence.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

/**
 * 
 * User등록dto 클래스
 */
data class RegisterUserRequestDto(

    @field:NotBlank
    @Schema(description = "이름")
    var name: String,

    @field:NotBlank
    @Schema(description = "비밀번호")
    var password : String,

    @field:NotBlank
    @Schema(description = "핸드폰 번호", example = "01012345678")
    var phoneNumber: String,

    @field:NotBlank
    @Schema(description = "이메일", example = "xxx@example.com")
    var email: String,

    @field:NotNull
    @Schema(description = "생년월일", example = "19900101")
    var birth: String,
){
    fun toEntity(id : Long) : UserEntity {
        return UserEntity(
            id = id,
            name = this.name,
            password = this.password,
            phoneNumber = this.phoneNumber,
            ci = "",
            email = this.email,
            birth = this.birth,
            isDelete = false,
            createAt = LocalDateTime.now(),
            updateAt = null,
            deleteAt = null,
            emailVerified = false,
        )
    }

}