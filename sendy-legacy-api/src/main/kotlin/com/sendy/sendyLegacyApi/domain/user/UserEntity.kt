package com.sendy.sendyLegacyApi.domain.user

import com.sendy.sendyLegacyApi.application.dto.user.UpdateUserDto
import com.sendy.sendyLegacyApi.infrastructure.persistence.Identity
import com.sendy.sendyLegacyApi.support.util.Aes256Converter
import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *
 * jpa entity
 */
@Entity
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(name = "users_phone_number_uk", columnNames = ["phone_number"]),
    ],
)
class UserEntity(
    id: Long,
    @Column(name = "name", length = 50, nullable = false)
    val name: String,
    @Column(name = "phone_number", length = 100, nullable = false)
    @Convert(converter = Aes256Converter::class)
    val phoneNumber: String,
    @Column(name = "password", length = 255, nullable = false)
    val password: String,
    @Column(name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    @Convert(converter = Aes256Converter::class)
    val email: String,
    @Column(name = "ci", length = 100, nullable = true)
    @Convert(converter = Aes256Converter::class)
    val ci: String? = null,
    @Column(name = "birth", nullable = false, columnDefinition = "CHAR(8)")
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
    var emailVerified: Boolean = false,
) : Identity(id) {
    fun update(updateDto: UpdateUserDto): UserEntity =
        UserEntity(
            id = this.id,
            password = this.password,
            name = updateDto.name ?: this.name,
            phoneNumber = updateDto.phoneNumber ?: this.phoneNumber,
            email = updateDto.email ?: this.email,
            ci = this.ci,
            birth = this.birth,
            isDelete = this.isDelete,
            emailVerified = this.emailVerified,
            createAt = this.createAt,
            updateAt = LocalDateTime.now(),
            deleteAt = this.deleteAt,
        )

    fun deleteUser(): UserEntity =
        UserEntity(
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

    fun updateActivity(): UserEntity =
        UserEntity(
            id = this.id,
            password = this.password,
            name = this.name,
            phoneNumber = this.phoneNumber,
            email = this.email,
            ci = this.ci,
            birth = this.birth,
            isDelete = this.isDelete,
            emailVerified = this.emailVerified,
            createAt = this.createAt,
            updateAt = LocalDateTime.now(),
            deleteAt = this.deleteAt,
        )

    /**
     * 이메일 인증 완료 처리
     */
    fun verifyEmail() {
        this.emailVerified = true
        this.updateAt = LocalDateTime.now()
    }

    /**
     * 사용자 활성화 상태 확인
     */
    fun canLogin(): Boolean = !isDelete && emailVerified

    /**
     * 비밀번호 검증
     */
    fun validatePassword(inputPassword: String): Boolean = password == inputPassword

    /**
     * 사용자 활성화 상태 확인
     */
    fun isActive(): Boolean = !isDelete

    /**
     * 마지막 업데이트 시간 갱신
     */
    fun updateLastActivity() {
        this.updateAt = LocalDateTime.now()
    }
}
