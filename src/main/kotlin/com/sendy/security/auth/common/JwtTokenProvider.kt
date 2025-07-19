package com.sendy.security.auth.common

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret-key}") private val secretKey: String,
    @Value("\${jwt.access-token-expire-time}") private val validityInSeconds: Long,
    private val userDetailsService: UserDetailsService,

    ) {
    private val key = Keys.hmacShaKeyFor(secretKey.toByteArray())

    fun createToken(userId: Long, role: String): String {
        val claims = Jwts.claims().setSubject(userId.toString()).apply {
            put("role", role)
        }
        val now = Date()
        val validity = Date(now.time + validityInSeconds * 1000)
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }
    fun validateToken(token: String): Boolean =try {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token)
        true
    } catch(e: Exception){
        false
    }
    fun getUserId(token: String): String {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJwt(token).body
        return claims.subject
    }
    fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val userId = getUserId(token)
        val userDetails = userDetailsService.loadUserByUsername(userId)
        return UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
    }
}