package com.sendy.email.model

import jakarta.persistence.*

/**
 *
 * jpa entity
 */

data class EmailDto (

    var emailId: Long,

    var code: String,

    var email: String,

    var isVerified: Boolean = false,

){
    fun toEntity(
    ): Email {
        return Email(
            emailId = this.emailId,
            code = this.code,
            email = this.email,
            isVerified = this.isVerified
        )
    }
}

