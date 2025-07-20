package com.sendy.domain.repository

import com.sendy.infrastructure.persistence.JwtTokenEntity
import com.sendy.infrastructure.persistence.TokenType
import com.sendy.infrastructure.persistence.TokenStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

/**
 * JWT 토큰 관리를 위한 리포지토리
 */
@Repository
interface JwtTokenRepository : JpaRepository<JwtTokenEntity, Long> {
    
    /**
     * 토큰 해시로 유효한 토큰 찾기 (ACTIVE 또는 PENDING_LOGOUT)
     */
    fun findByTokenHashAndStatusIn(tokenHash: String, statuses: List<TokenStatus>): Optional<JwtTokenEntity>
    
    /**
     * 토큰 해시로 활성 토큰만 찾기
     */
    fun findByTokenHashAndStatus(tokenHash: String, status: TokenStatus): Optional<JwtTokenEntity>
    
    /**
     * 사용자의 특정 타입 활성 토큰 찾기
     */
    fun findByUserIdAndTokenTypeAndStatus(userId: Long, tokenType: TokenType, status: TokenStatus): List<JwtTokenEntity>
    
    /**
     * 사용자의 모든 토큰을 REVOKED 상태로 변경
     */
    @Modifying
    @Query("UPDATE JwtTokenEntity j SET j.status = 'REVOKED', j.updatedAt = :updatedAt WHERE j.userId = :userId AND j.status != 'REVOKED'")
    fun revokeAllTokensByUserId(userId: Long, updatedAt: LocalDateTime)
    
    /**
     * 사용자의 모든 토큰을 PENDING_LOGOUT 상태로 변경
     */
    @Modifying
    @Query("UPDATE JwtTokenEntity j SET j.status = 'PENDING_LOGOUT', j.updatedAt = :updatedAt WHERE j.userId = :userId AND j.status = 'ACTIVE'")
    fun setPendingLogoutByUserId(userId: Long, updatedAt: LocalDateTime)
    
    /**
     * 특정 토큰을 REVOKED 상태로 변경
     */
    @Modifying
    @Query("UPDATE JwtTokenEntity j SET j.status = 'REVOKED', j.updatedAt = :updatedAt WHERE j.tokenHash = :tokenHash")
    fun revokeTokenByHash(tokenHash: String, updatedAt: LocalDateTime)
    
    /**
     * 특정 토큰을 PENDING_LOGOUT 상태로 변경
     */
    @Modifying
    @Query("UPDATE JwtTokenEntity j SET j.status = 'PENDING_LOGOUT', j.updatedAt = :updatedAt WHERE j.tokenHash = :tokenHash")
    fun setPendingLogoutByHash(tokenHash: String, updatedAt: LocalDateTime)
    
    /**
     * 만료된 토큰들 삭제
     */
    @Modifying
    @Query("DELETE FROM JwtTokenEntity j WHERE j.expiredAt < :currentTime")
    fun deleteExpiredTokens(currentTime: LocalDateTime)
    
    /**
     * REVOKED 상태의 토큰들 삭제
     */
    @Modifying
    @Query("DELETE FROM JwtTokenEntity j WHERE j.status = 'REVOKED'")
    fun deleteRevokedTokens()
    
    /**
     * 특정 시간 이전에 REVOKED된 토큰들 삭제
     */
    @Modifying
    @Query("DELETE FROM JwtTokenEntity j WHERE j.status = 'REVOKED' AND j.updatedAt < :beforeTime")
    fun deleteRevokedTokensBefore(beforeTime: LocalDateTime)
    
    /**
     * 사용자의 REVOKED 토큰들 삭제
     */
    @Modifying
    @Query("DELETE FROM JwtTokenEntity j WHERE j.userId = :userId AND j.status = 'REVOKED'")
    fun deleteRevokedTokensByUserId(userId: Long)
    
    /**
     * 사용자의 특정 타입 토큰을 REVOKED 상태로 변경
     */
    @Modifying
    @Query("UPDATE JwtTokenEntity j SET j.status = 'REVOKED', j.updatedAt = :updatedAt WHERE j.userId = :userId AND j.tokenType = :tokenType AND j.status != 'REVOKED'")
    fun revokeTokensByUserIdAndType(userId: Long, tokenType: TokenType, updatedAt: LocalDateTime)
    
    /**
     * 특정 디바이스의 모든 토큰을 REVOKED 상태로 변경
     */
    @Modifying
    @Query("UPDATE JwtTokenEntity j SET j.status = 'REVOKED', j.updatedAt = :updatedAt WHERE j.deviceId = :deviceId AND j.status != 'REVOKED'")
    fun revokeTokensByDeviceId(deviceId: Long, updatedAt: LocalDateTime)
    
    /**
     * 사용자의 PENDING_LOGOUT 상태 토큰들 조회
     */
    fun findByUserIdAndStatus(userId: Long, status: TokenStatus): List<JwtTokenEntity>
} 