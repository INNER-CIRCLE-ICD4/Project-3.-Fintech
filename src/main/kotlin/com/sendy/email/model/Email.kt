package com.sendy.email.model

import jakarta.persistence.*

/**
 *
 * jpa entity
 */
@Entity
@Table(name="email_auth")
class Email (

    @Id
    @Column(name = "email_id")
    val emailId: Long,

    @Column(name = "code")
    val code: String,

    val email: String,

    @Column(name = "is_verified")
    var isVerified: Boolean,

)

