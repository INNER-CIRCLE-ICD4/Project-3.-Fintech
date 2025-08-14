package com.sendy.sendyLegacyApi.application.dto.email

import com.sendy.sendyLegacyApi.domain.email.EmailEntity
import java.time.LocalDateTime

/**
 *
 * jpa entity
 */

data class EmailDto(
    val id: Long,
    val code: String,
    val email: String,
    val isVerified: Boolean = false,
    val userId: Long,
    val sendAt: LocalDateTime,
) {
    fun toEntity(): EmailEntity =
        EmailEntity(
            id = this.id,
            code = this.code,
            email = this.email,
            isVerified = this.isVerified,
            userId = this.userId,
            sendAt = this.sendAt,
        )
}
