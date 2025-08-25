package com.sendy.sendyLegacyApi.domain.authorities

import com.sendy.sendyLegacyApi.infrastructure.persistence.auth.DeviceInfoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.*

/**
 * 디바이스 정보 관리를 위한 리포지토리
 */
interface DeviceInfoRepository : JpaRepository<DeviceInfoEntity, Long> {
    /**
     * 디바이스 핑거프린트로 디바이스 찾기
     */
    fun findByDeviceFingerprint(deviceFingerprint: String): Optional<DeviceInfoEntity>

    /**
     * 사용자의 모든 디바이스 조회
     */
    @Query("SELECT d FROM DeviceInfoEntity d WHERE d.user.id = :userId")
    fun findByUserId(userId: Long): List<DeviceInfoEntity>

    /**
     * 디바이스의 마지막 로그인 시간 업데이트
     */
    @Modifying
    @Query("UPDATE DeviceInfoEntity d SET d.lastLoginAt = :loginTime, d.updatedAt = :updatedAt WHERE d.id = :deviceId")
    fun updateLastLoginTime(
        deviceId: Long,
        loginTime: LocalDateTime,
        updatedAt: LocalDateTime,
    )
}
