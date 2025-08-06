package com.sendy.domain.auth

import com.sendy.infrastructure.persistence.auth.JwtRefreshTokenEntity
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


/**
 * 레디스에 저장된 Refresh Token을 관리하는 리포지토리
 */
@Repository
interface JwtRefreshTokenRepository : CrudRepository<JwtRefreshTokenEntity?, Long?> {
    fun findByTokenType(refreshToken: String?): Optional<JwtTokenRepository?>?
}