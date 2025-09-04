package com.sendy.sendyLegacyApi.domain.email

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
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
    var code: String,
    @Column(name = "email")
    val email: String,
    @Column(name = "is_verified")
    var isVerified: Boolean,
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "send_at")
    var sendAt: LocalDateTime,
) {
    fun sendEmailUpdate(newCode: String) {
        this.isVerified = false
        this.code = newCode
        this.sendAt = LocalDateTime.now()
    }
}
