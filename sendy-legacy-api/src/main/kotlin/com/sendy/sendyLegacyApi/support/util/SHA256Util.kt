package com.sendy.sendyLegacyApi.support.util

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import java.nio.charset.StandardCharsets

/**
 * SHA-256 해시 유틸리티
 */

@Component
class SHA256Util {

    companion object {
        private const val SALT_LENGTH = 32 // 32바이트 salt
        private const val SEPARATOR = ":"
    }

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_LENGTH)
        SecureRandom().nextBytes(salt)
        return salt
    }

    private fun ByteArray.toHex(): String =
        joinToString("") { "%02x".format(it) }

    private fun ByteArray.toBase64(): String =
        Base64.getEncoder().encodeToString(this)

    private fun sha256WithSalt(password: String, salt: ByteArray): ByteArray {
        val md = MessageDigest.getInstance("SHA-256")
        md.update(salt)
        md.update(password.toByteArray(StandardCharsets.UTF_8))
        return md.digest()
    }

    private fun constantTimeEquals(a: ByteArray?, b: ByteArray?): Boolean {
        if (a == null || b == null) return false
        if (a.size != b.size) return false
        var r = 0
        for (i in a.indices) r = r or (a[i].toInt() xor b[i].toInt())
        return r == 0
    }

    /**
     * 기본 해시 메서드 (Salt 포함)
     * 반환 형식: "base64Salt:hexHash"
     */
    fun hash(input: String): String {
        val salt = generateSalt()
        val hash = sha256WithSalt(input, salt)
        return "${salt.toBase64()}$SEPARATOR${hash.toHex()}"
    }

    /**
     * 비밀번호 검증 (하위 호환성 지원)
     */
    fun matches(rawValue: String, storedHash: String): Boolean {
        return if (storedHash.contains(SEPARATOR)) {
            // 새로운 Salt 방식
            val parts = storedHash.split(SEPARATOR)
            if (parts.size != 2) return false

            try {
                val salt = Base64.getDecoder().decode(parts[0])
                val expectedHashHex = parts[1]
                val actualHash = sha256WithSalt(rawValue, salt)
                val actualHashHex = actualHash.toHex()
                actualHashHex == expectedHashHex
            } catch (e: Exception) {
                false
            }
        } else {
            // 기존 방식 (하위 호환성)
            hashLegacy(rawValue) == storedHash
        }
    }

    /**
     * 기존 방식의 해시 (하위 호환성용)
     */
    private fun hashLegacy(input: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(input.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * 고정된 해시 생성 (디바이스 핑거프린트용)
     * 같은 입력에 대해 항상 같은 결과를 보장 (Salt 사용 안함)
     */
    fun createFixedHash(input: String): String = hashLegacy(input)
}