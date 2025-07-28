package com.sendy.application.dto
import com.sendy.domain.user.UserEntity
import com.sendy.infrastructure.persistence.Identity
import com.sendy.support.util.getTsid
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import java.time.LocalDateTime

/**
 *
 * User등록dto 클래스
 */
data class CreateUserDto(

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
    @Schema(description = "이메일", example = "test@gmail.com")
    var email: String,

    @field:NotNull
    @Schema(description = "생년월일", example = "19900101")
    var birth: String,
){
    fun toEntity(id : Long,password: String) : UserEntity {
        return UserEntity(
            id = id,
            name = this.name,
            password = password,
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