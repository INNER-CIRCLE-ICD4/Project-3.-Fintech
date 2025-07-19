package com.sendy.infrastructure.persistence

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 *
 * jpa entity
 */
@Entity
@Table(name = "users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    var id: Long = 0,

    @Column(name = "name", length = 50, nullable = false)
    val name: String,

    @Column(name = "phone_number", length = 20, nullable = false)
    val phoneNumber: String,

    @Column(name = "password", length = 100, nullable = false)
    val password: String,

    @Column(name = "email", columnDefinition = "VARCHAR(255)", nullable = false)
    val email: String,

    @Column(name = "ci", length = 100, nullable = true)
    val ci: String? = null,

    @Column(name = "birth", nullable = false)
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
    var emailVerified: Boolean = false
    )
