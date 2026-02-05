package com.sendy.sendyLegacyApi.infrastructure.persistence.auth

import com.sendy.sendyLegacyApi.domain.enum.TokenStatus
import com.sendy.sendyLegacyApi.domain.enum.TokenType
import jakarta.persistence.*
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDateTime

/**
 * redis에 refesth 토큰 저장을 위한 엔티티 / 데이터 7일후 자동삭제
 */
@RedisHash(value = "refresh_token", timeToLive = 604800)
data class JwtRefreshTokenEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    var id: Long? = null,
    @Column(name = "user_id", nullable = false)
    val userId: Long,
    @Column(name = "device_id", nullable = true)
    val deviceId: Long? = null,
    @Column(name = "token_hash", length = 500, nullable = false)
    val tokenHash: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false)
    val tokenType: TokenType = TokenType.REFRESH,
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
