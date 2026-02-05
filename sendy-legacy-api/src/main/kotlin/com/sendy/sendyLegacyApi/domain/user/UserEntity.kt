package com.sendy.sendyLegacyApi.domain.user

import com.sendy.sendyLegacyApi.application.dto.users.UpdateUserDto
import com.sendy.sendyLegacyApi.infrastructure.persistence.Identity
import com.sendy.sendyLegacyApi.support.error.ErrorCode
import com.sendy.sendyLegacyApi.support.exception.ServiceException
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
    var name: String,
    @Column(name = "phone_number", length = 255, nullable = false)
    @Convert(converter = Aes256Converter::class)
    var phoneNumber: String,
    @Column(name = "password", length = 255, nullable = false)
    var password: String,
    @Column(name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    @Convert(converter = Aes256Converter::class)
    var email: String,
    @Column(name = "ci", length = 100, nullable = true)
    @Convert(converter = Aes256Converter::class)
    val ci: String? = null,
    @Column(name = "birth", nullable = false, columnDefinition = "CHAR(8)")
    var birth: String = "", // YYYYMMDD format with default value
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
    @Column(name = "is_locked", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    var isLocked: Boolean = false,
    @Column(name = "wrong_count")
    var wrongCount: Int,
) : Identity(id) {
    fun update(updateDto: UpdateUserDto): UserEntity =
        this.apply {
            password = updateDto.password ?: this.password
            name = updateDto.name ?: this.name
            phoneNumber = updateDto.phoneNumber ?: this.phoneNumber
            email = updateDto.email ?: this.email
            birth = updateDto.birth ?: this.birth
            updateAt = LocalDateTime.now()
        }

    /**
     * 사용자 활성화 상태 확인
     */
    fun canLogin(): Boolean = !isDelete && emailVerified

    fun userDelete(): Boolean = !isDelete

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

    fun checkSelfAndInvokeError(phoneNumber: String) {
        if (this.phoneNumber == phoneNumber) {
            throw ServiceException(ErrorCode.INVALID_INPUT_VALUE)
        }
    }

    /**
     * 사용자 잠금 처리
     */
    fun userLocked(): Boolean = !isLocked
}
