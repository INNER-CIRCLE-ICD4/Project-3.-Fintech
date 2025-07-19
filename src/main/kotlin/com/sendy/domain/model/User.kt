package com.sendy.domain.model

import java.time.LocalDateTime

/**
 * pure domain model (JPA 엔티티가 아님)
 */
data class User(
    val id: Long = 0,
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String? = null,
    val isDelete: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)