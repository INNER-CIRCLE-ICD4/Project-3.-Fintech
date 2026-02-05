package com.sendy.sendyLegacyApi.application.usecase.authorities

import com.sendy.sendyLegacyApi.domain.authorities.JwtRefreshTokenRepository
import com.sendy.sendyLegacyApi.domain.authorities.JwtTokenRepository
import com.sendy.sendyLegacyApi.domain.enum.TokenStatus
import com.sendy.sendyLegacyApi.domain.enum.TokenType
import com.sendy.sendyLegacyApi.infrastructure.persistence.auth.JwtRefreshTokenEntity
import com.sendy.sendyLegacyApi.infrastructure.persistence.auth.JwtTokenEntity
import com.sendy.sendyLegacyApi.support.util.SHA256Util
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * JWT 토큰의 데이터베이스 저장과 관리를 담당하는 서비스
 */
@Service
@Transactional
class JwtTokenStorageService(
    private val jwtTokenRepository: JwtTokenRepository,
    private val jwtRefreshTokenRepository: JwtRefreshTokenRepository,
    private val sha256Util: SHA256Util
) {
    /**
     * 토큰을 데이터베이스에 저장 (jti 사용)
     */
    fun saveToken(
        userId: Long,
        jti: String,
        tokenType: TokenType,
        expiredAt: LocalDateTime,
    ): JwtTokenEntity {
        // 기존의 같은 타입 토큰들 무효화 (예: 새로운 access token 발급 시 기존 access token 무효화)
        revokeTokensByUserIdAndType(userId, tokenType)

        val jwtTokenEntity =
            JwtTokenEntity(
                userId = userId,
                tokenHash = jti, // jti를 tokenHash 필드에 저장
                tokenType = tokenType,
                expiredAt = expiredAt,
                status = TokenStatus.ACTIVE,
            )

        return jwtTokenRepository.save(jwtTokenEntity)
    }

    /**
     * 리프레시 토큰을 레디스에 저장 (jti 사용)
     */
    fun saveRefreshToken(
        userId: Long,
        jti: String,
        tokenType: TokenType,
        expiredAt: LocalDateTime,
    ) {
        // 기존의 같은 타입 토큰들 무효화 (예: 새로운 access token 발급 시 기존 access token 무효화)
        revokeTokensByUserIdAndType(userId, tokenType)

        jwtRefreshTokenRepository.save(
            JwtRefreshTokenEntity(
                userId = userId,
                tokenHash = jti, // jti를 tokenHash 필드에 저장
                expiredAt = expiredAt,
                status = TokenStatus.ACTIVE,
            ),
        )
    }

    /**
     * 토큰을 데이터베이스에 저장 (디바이스 ID 포함, jti 사용)
     */
    fun saveToken(
        userId: Long,
        deviceId: Long,
        jti: String,
        tokenType: TokenType,
        expiredAt: LocalDateTime,
    ): JwtTokenEntity {
        // 기존의 같은 타입 토큰들 무효화 (예: 새로운 access token 발급 시 기존 access token 무효화)
        revokeTokensByUserIdAndType(userId, tokenType)

        val jwtTokenEntity =
            JwtTokenEntity(
                userId = userId,
                deviceId = deviceId,
                tokenHash = jti, // jti를 tokenHash 필드에 저장
                tokenType = tokenType,
                expiredAt = expiredAt,
                status = TokenStatus.ACTIVE,
            )

        return jwtTokenRepository.save(jwtTokenEntity)
    }

    /**
     * 리프레시토큰을 데이터베이스에 저장 (디바이스 ID 포함, jti 사용)
     */
    fun saveRefreshToken(
        userId: Long,
        deviceId: Long,
        jti: String,
        tokenType: TokenType,
        expiredAt: LocalDateTime,
    ) {
        // 기존의 같은 타입 토큰들 무효화 (예: 새로운 access token 발급 시 기존 access token 무효화)
        revokeTokensByUserIdAndType(userId, tokenType)

        jwtRefreshTokenRepository.save(
            JwtRefreshTokenEntity(
                userId = userId,
                tokenHash = jti, // jti를 tokenHash 필드에 저장
                expiredAt = expiredAt,
                status = TokenStatus.ACTIVE
            )
        )
    }

    /**
     * 토큰이 완전히 유효한지 확인 (ACTIVE 상태만, jti 사용)
     */
    @Transactional(readOnly = true)
    fun isTokenValid(jti: String): Boolean {
        val tokenEntity = jwtTokenRepository.findByTokenHashAndStatus(jti, TokenStatus.ACTIVE)

        return tokenEntity.isPresent &&
                tokenEntity.get().expiredAt.isAfter(LocalDateTime.now())
    }

    /**
     * 토큰 상태 확인 (ACTIVE, PENDING_LOGOUT, REVOKED 구분, jti 사용)
     */
    @Transactional(readOnly = true)
    fun getTokenStatus(jti: String): TokenStatus? {
        val tokenEntity =
            jwtTokenRepository.findByTokenHashAndStatusIn(
                jti,
                listOf(TokenStatus.ACTIVE, TokenStatus.PENDING_LOGOUT, TokenStatus.REVOKED),
            )

        return if (tokenEntity.isPresent && tokenEntity.get().expiredAt.isAfter(LocalDateTime.now())) {
            tokenEntity.get().status
        } else {
            null
        }
    }

    /**
     * 토큰으로 사용자 ID 조회 (ACTIVE 상태만, jti 사용)
     */
    @Transactional(readOnly = true)
    fun getUserIdByToken(jti: String): Long? {
        val tokenEntity = jwtTokenRepository.findByTokenHashAndStatus(jti, TokenStatus.ACTIVE)

        return if (tokenEntity.isPresent && tokenEntity.get().expiredAt.isAfter(LocalDateTime.now())) {
            tokenEntity.get().userId
        } else {
            null
        }
    }

    /**
     * 토큰으로 사용자 ID 조회 (PENDING_LOGOUT 상태 포함, jti 사용)
     */
    @Transactional(readOnly = true)
    fun getUserIdByTokenForPending(jti: String): Long? {
        val tokenEntity =
            jwtTokenRepository.findByTokenHashAndStatusIn(
                jti,
                listOf(TokenStatus.ACTIVE, TokenStatus.PENDING_LOGOUT),
            )

        return if (tokenEntity.isPresent && tokenEntity.get().expiredAt.isAfter(LocalDateTime.now())) {
            tokenEntity.get().userId
        } else {
            null
        }
    }

    /**
     * 특정 토큰을 REVOKED 상태로 변경 (jti 사용)
     */
    fun revokeToken(jti: String) {
        jwtTokenRepository.revokeTokenByHash(jti, LocalDateTime.now())
    }

    /**
     * 토큰 해시로 직접 토큰을 REVOKED 상태로 변경
     */
    fun revokeTokenByHash(tokenHash: String) {
        jwtTokenRepository.revokeTokenByHash(tokenHash, LocalDateTime.now())
    }

    /**
     * 사용자의 모든 토큰을 REVOKED 상태로 변경 (완전 로그아웃)
     */
    fun revokeAllTokensByUserId(userId: Long) {
        jwtTokenRepository.revokeAllTokensByUserId(userId, LocalDateTime.now())
    }

    /**
     * 사용자의 모든 토큰을 PENDING_LOGOUT 상태로 변경 (로그아웃 대기)
     */
    fun setPendingLogoutByUserId(userId: Long) {
        jwtTokenRepository.setPendingLogoutByUserId(userId, LocalDateTime.now())
    }

    /**
     * 특정 토큰을 PENDING_LOGOUT 상태로 변경 (jti 사용)
     */
    fun setPendingLogoutByToken(jti: String) {
        jwtTokenRepository.setPendingLogoutByHash(jti, LocalDateTime.now())
    }

    /**
     * 사용자의 PENDING_LOGOUT 상태 토큰들을 ACTIVE로 복원 (로그아웃 거부)
     */
    fun restorePendingTokensByUserId(userId: Long) {
        val pendingTokens = jwtTokenRepository.findByUserIdAndStatus(userId, TokenStatus.PENDING_LOGOUT)
        pendingTokens.forEach { token ->
            token.status = TokenStatus.ACTIVE
            token.updatedAt = LocalDateTime.now()
        }
        jwtTokenRepository.saveAll(pendingTokens)
    }

    /**
     * 사용자의 특정 타입 토큰들을 REVOKED 상태로 변경
     */
    fun revokeTokensByUserIdAndType(
        userId: Long,
        tokenType: TokenType,
    ) {
        jwtTokenRepository.revokeTokensByUserIdAndType(userId, tokenType, LocalDateTime.now())
    }

    /**
     * 특정 디바이스의 모든 토큰을 REVOKED 상태로 변경
     */
    fun revokeTokensByDeviceId(deviceId: Long) {
        jwtTokenRepository.revokeTokensByDeviceId(deviceId, LocalDateTime.now())
    }

    /**
     * 만료된 토큰들 정리
     */
    fun cleanupExpiredTokens() {
        jwtTokenRepository.deleteExpiredTokens(LocalDateTime.now())
    }

    /**
     * REVOKED 상태의 모든 토큰들 삭제
     */
    fun cleanupRevokedTokens() {
        jwtTokenRepository.deleteRevokedTokens()
    }

    /**
     * 특정 시간 이전에 REVOKED된 토큰들 삭제
     */
    fun cleanupRevokedTokensBefore(beforeTime: LocalDateTime) {
        jwtTokenRepository.deleteRevokedTokensBefore(beforeTime)
    }

    /**
     * 특정 사용자의 REVOKED 토큰들 삭제
     */
    fun cleanupRevokedTokensByUserId(userId: Long) {
        jwtTokenRepository.deleteRevokedTokensByUserId(userId)
    }

    /**
     * 모든 정리 작업 수행 (만료된 토큰 + REVOKED 토큰)
     */
    fun cleanupAllTokens() {
        val now = LocalDateTime.now()
        jwtTokenRepository.deleteExpiredTokens(now)
        jwtTokenRepository.deleteRevokedTokens()
    }
}
