package com.sendy.infrastructure.persistence.auth

import jakarta.persistence.*
import java.time.LocalDateTime

/**
 * 디바이스 정보를 저장하는 엔티티
 */
@Entity
@Table(name = "device_info")
data class DeviceInfoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id")
    var id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
    @Column(name = "device_fingerprint", length = 255, nullable = false, unique = true)
    val deviceFingerprint: String,
    @Column(name = "device_name", length = 100, nullable = true)
    val deviceName: String? = null,
    @Column(name = "user_agent", length = 500, nullable = true)
    val userAgent: String? = null,
    @Column(name = "ip_address", length = 50, nullable = true)
    val ipAddress: String? = null,
    @Column(name = "browser_info", length = 200, nullable = true)
    val browserInfo: String? = null,
    @Column(name = "os_info", length = 100, nullable = true)
    val osInfo: String? = null,
    @Column(name = "screen_resolution", length = 20, nullable = true)
    val screenResolution: String? = null,
    @Column(name = "timezone", length = 50, nullable = true)
    val timezone: String? = null,
    @Column(name = "language", length = 10, nullable = true)
    val language: String? = null,
    @Column(name = "is_mobile", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    val isMobile: Boolean = false,
    @Column(name = "last_login_at", nullable = false)
    var lastLoginAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
    @Column(name = "updated_at", nullable = true)
    var updatedAt: LocalDateTime? = null,
) {
    /**
     * userId 편의 프로퍼티 (기존 코드 호환성을 위해)
     */
    val userId: Long
        get() = user.id
} 
