package com.sendy.sendyLegacyApi.support.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class Aes256Util(
    @Value("\${aes256.key}")
    key: String,
) {
    private lateinit var secretKey: SecretKeySpec

    companion object {
        private const val ALGORITHM = "AES"
        private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
        private const val IV_SIZE = 16
        private const val KEY_SIZE = 32
    }

    init {
        require(key.length == KEY_SIZE) { "AES-256 알고리즘 사용 시 32바이트 키가 필수입니다." }
        secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
    }

    fun encrypt(plainText: String): String =
        try {
            val iv = ByteArray(IV_SIZE)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
            val encrypted = cipher.doFinal(plainText.toByteArray())
            Base64.getEncoder().encodeToString(encrypted)
        } catch (e: Exception) {
            throw RuntimeException("AES encryption error", e)
        }

    fun decrypt(cipherText: String): String =
        try {
            val ivAndEncrypted = Base64.getDecoder().decode(cipherText)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(ByteArray(IV_SIZE)))
            val decrypted = cipher.doFinal(ivAndEncrypted)
            String(decrypted)
        } catch (e: Exception) {
            throw RuntimeException("AES decryption error", e)
        }
}
