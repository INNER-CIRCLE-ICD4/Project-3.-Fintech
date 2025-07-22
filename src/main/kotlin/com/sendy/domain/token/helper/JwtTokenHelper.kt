package com.sendy.domain.token.helper

import com.common.domain.error.TokenErrorCode
import com.common.domain.exceptions.ApiException
import com.sendy.domain.token.ifs.TokenHelperIfs
import com.sendy.domain.token.model.TokenDto
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.sql.Date
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Component
class JwtTokenHelper(
    @Value("\${jwt.secret-key}") private val secretKey: String,
    @Value("\${jwt.access-token-expire-time}") private val accessTokenExpireTime: Long,
    @Value("\${jwt.refresh-token-expire-time}") private val refreshTokenExpireTime: Long
) : TokenHelperIfs {

    override fun issueAccessToken(data: Map<String, Any>): TokenDto {
        val expiredLocalDateTime = LocalDateTime.now().plusHours(accessTokenExpireTime)
        val expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant())
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray())
        
        // 고유한 JWT ID 생성
        val jti = UUID.randomUUID().toString()

        val jwtToken = Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)
            .setClaims(data)
            .setId(jti) // JWT ID 설정
            .setExpiration(expiredAt)
            .compact()

        return TokenDto(
            token = jwtToken,
            jti = jti, // JWT ID 반환
            expiredAt = expiredLocalDateTime
        )
    }

    override fun issueRefreshToken(data: Map<String, Any>): TokenDto {
        val expiredLocalDateTime = LocalDateTime.now().plusHours(refreshTokenExpireTime)
        val expiredAt = Date.from(expiredLocalDateTime.atZone(ZoneId.systemDefault()).toInstant())
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray())
        
        // 고유한 JWT ID 생성
        val jti = UUID.randomUUID().toString()

        val jwtToken = Jwts.builder()
            .signWith(key, SignatureAlgorithm.HS256)
            .setClaims(data)
            .setId(jti) // JWT ID 설정
            .setExpiration(expiredAt)
            .compact()

        return TokenDto(
            token = jwtToken,
            jti = jti, // JWT ID 반환
            expiredAt = expiredLocalDateTime
        )
    }

    override fun validationTokenWithThrow(token: String): Map<String, Any> {
        val key = Keys.hmacShaKeyFor(secretKey.toByteArray())
        val parser = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()

        return try {
            val result = parser.parseClaimsJws(token)
            HashMap(result.body)
        } catch (e: SignatureException) {
            throw ApiException(TokenErrorCode.INVALID_TOKEN, e)
        } catch (e: ExpiredJwtException) {
            throw ApiException(TokenErrorCode.EXPIRED_TOKEN, e)
        } catch (e: Exception) {
            throw ApiException(TokenErrorCode.TOKEN_EXCEPTION, e)
        }
    }
}