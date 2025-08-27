package com.sendy.sendyLegacyApi.domain.authorities

import com.sendy.sendyLegacyApi.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

/**
 * UserEntity repository (인프라스트럭처 계층용)
 */
interface UserEntityRepository : JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String): UserEntity?

    @Query(
        value =
            "select user " +
                "from UserEntity user " +
                "where 1=1 " +
                "and user.phoneNumber = :phoneNumber " +
                "and user.deleteAt is null ",
    )
    fun findByPhoneNumber(
        @Param("phoneNumber") phoneNumber: String,
    ): UserEntity?

    fun findByIdAndIsDeleteFalse(id: Long): Optional<UserEntity>

    fun findByPhoneNumberAndDeleteAtIsNull(phoneNumber: String): UserEntity?

    fun findActiveById(id: Long): UserEntity?
    
}
