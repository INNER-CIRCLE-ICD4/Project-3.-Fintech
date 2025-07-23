package com.sendy.application.dto.user
import com.sendy.infrastructure.persistence.auth.UserEntity
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

/**
 *
 * User수정dto 클래스
 */
data class UpdateUserRequestDto(

    var name: String,

    var password : String,

    @Schema(description = "핸드폰 번호", example = "01012345678")
    var phoneNumber: String,

    @Schema(description = "이메일", example = "xxx@example.com")
    var email: String,

    @Schema(description = "생년월일", example = "19900101")
    var birth: String,
){
    fun toEntity(id : Long) : UserEntity {
        return UserEntity(
            id = id,
            name = this.name,
            password = "",
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