package com.sendy.application.dto.email

import com.sendy.domain.email.EmailEntity

/**
 *
 * jpa entity
 */

data class EmailDto(
    var emailId: Long,
    var code: String,
    var email: String,
    var isVerified: Boolean = false,
    var userId: Long
) {
    fun toEntity(): EmailEntity =
        EmailEntity(
            emailId = this.emailId,
            code = this.code,
            email = this.email,
            isVerified = this.isVerified,
            userId = this.userId
        )
}
