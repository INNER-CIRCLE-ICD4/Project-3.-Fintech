package com.sendy.domain.email

import com.sendy.support.util.Aes256Converter
import jakarta.persistence.*

/**
 *
 * jpa entity
 */
@Entity
@Table(name = "email_auth")
class EmailEntity(
    @Id
    @Column(name = "email_id")
    val emailId: Long,

    @Column(name = "code")
    val code: String,

    @Column(name = "email")
    val email: String,

    @Column(name = "is_verified")
    var isVerified: Boolean,

    @Column(name = "user_id")
    val userId: Long,
)
