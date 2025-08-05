package com.sendy.support.util

import org.springframework.stereotype.Component
import java.security.MessageDigest

/**
 * SHA-256 해시 유틸리티
 */
@Component
class SHA256Util {
    /**
     * 문자열을 SHA-256으로 해시
     */
    fun hash(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * 고정된 해시 생성 (디바이스 핑거프린트용)
     * 같은 입력에 대해 항상 같은 결과를 보장
     */
    fun createFixedHash(input: String): String = hash(input)

    fun matches(
        rawValue: String,
        hashValue: String,
    ) = hash(rawValue) == hashValue
}
