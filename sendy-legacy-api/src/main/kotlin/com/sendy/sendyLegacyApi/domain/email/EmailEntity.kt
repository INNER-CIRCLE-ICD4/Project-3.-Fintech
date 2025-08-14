package com.sendy.sendyLegacyApi.domain.email

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *
 * jpa entity
 */
@Entity
@Table(name = "email")
class EmailEntity(
    @Id
    @Column(name = "id")
    val id: Long,
    @Column(name = "code")
    val code: String,
    @Column(name = "email")
    val email: String,
    @Column(name = "is_verified")
    var isVerified: Boolean,
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "send_at")
    val sendAt: LocalDateTime,
)
