package com.sendy.infrastructure.persistence

import com.sendy.support.util.Aes256Converter
import com.sendy.user.application.dto.UpdateUserRequestDto
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *
 * jpa entity
 */
@Entity
@Table(name = "users")
class UserEntity(


    id: Long,

    @Column(name = "name", length = 50, nullable = false)
    val name: String,

    @Column(name = "phone_number", length = 20, nullable = false)
    @Convert(converter = Aes256Converter::class)
    val phoneNumber: String,

    @Column(name = "password", length = 100, nullable = false)
    val password: String,

    @Column(name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    @Convert(converter = Aes256Converter::class)
    val email: String,

    @Column(name = "ci", length = 100, nullable = true)
    @Convert(converter = Aes256Converter::class)
    val ci: String? = null,

    @Column(name = "birth", nullable = false)
    val birth: String = "", // YYYYMMDD format with default value

    @Column(name = "is_delete", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    val isDelete: Boolean = false,

    @Column(name = "create_at", nullable = false)
    val createAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "update_at", nullable = true)
    var updateAt: LocalDateTime? = null,

    @Column(name = "delete_at", nullable = true)
    var deleteAt: LocalDateTime? = null,

    @Column(name = "email_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    var emailVerified: Boolean = false
) : Identity(id){
        fun update(updateDto : UpdateUserRequestDto): UserEntity {
        return UserEntity(
            id = this.id,
            password = this.password,
            name = if (updateDto.name == null) this.name else updateDto.name,
            phoneNumber = if(updateDto.phoneNumber == null) this.phoneNumber else updateDto.phoneNumber,
            email = if(updateDto.email == this.email) this.email else updateDto.email,
            ci = this.ci,
            birth = if(updateDto.birth == null) this.birth else updateDto.birth,
            isDelete = this.isDelete,
            emailVerified = this.emailVerified,
            createAt = this.createAt,
            updateAt = LocalDateTime.now(),
            deleteAt = this.deleteAt,
        )
    }
        fun deleteUser(): UserEntity {
        return UserEntity(
            id = this.id,
            password = this.password,
            name = this.name,
            phoneNumber = this.phoneNumber,
            email = this.email,
            ci = this.ci,
            birth = this.birth,
            isDelete = true,
            emailVerified = this.emailVerified,
            createAt = this.createAt,
            updateAt = this.createAt,
            deleteAt = LocalDateTime.now(),
        )
    }
}