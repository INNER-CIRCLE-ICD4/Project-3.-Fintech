package com.sendy.domain.auth

import com.sendy.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

/**
 * UserEntity repository (인프라스트럭처 계층용)
 */
interface UserEntityRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): Optional<UserEntity>

    fun findByPhoneNumber(phoneNumber: String): Optional<UserEntity>

    fun findByIdAndIsDeleteFalse(id: Long): Optional<UserEntity>

    fun findByPhoneNumberAndIsDeleteFalse(phoneNumber: String): Optional<UserEntity>

    fun existsByEmail(email: String): Boolean

    fun existsByPhoneNumber(phoneNumber: String): Boolean

    fun existsByEmailAndIsDeleteFalse(email: String): Boolean

    fun existsByPhoneNumberAndIsDeleteFalse(phoneNumber: String): Boolean

    fun existsByEmailAndPassword(
        email: String,
        password: String,
    ): Optional<UserEntity>
}
