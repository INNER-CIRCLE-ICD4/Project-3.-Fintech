package com.sendy.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

/**
 * pure domain model
 */
@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    val email : String,
    val password : String,
    val role: String = "USER" // 기본값으로 줬음. 권한별로 확장이 필요함
)