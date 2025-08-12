package com.sendy.infrastructure.persistence.auth

import com.sendy.domain.enum.TokenStatus
import com.sendy.domain.enum.TokenType
import jakarta.persistence.*
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDateTime

/**
 * JWT 토큰 저장을 위한 엔티티
 */
@Entity
@Table(name = "jwt_token")
data class JwtTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    var id: Long = 0,
    @Column(name = "user_id", nullable = false)
    val userId: Long,
    @Column(name = "device_id", nullable = true)
    val deviceId: Long? = null,
    @Column(name = "token_hash", length = 500, nullable = false)
    val tokenHash: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    val tokenType: TokenType = TokenType.ACCESS,
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    var status: TokenStatus = TokenStatus.ACTIVE,
    @Column(name = "expired_at", nullable = false)
    val expiredAt: LocalDateTime,
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at", nullable = true)
    var updatedAt: LocalDateTime? = null,
)
