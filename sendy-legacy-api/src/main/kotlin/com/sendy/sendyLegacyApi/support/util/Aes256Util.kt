package com.sendy.sendyLegacyApi.support.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@Component
class Aes256Util(
    @Value("{aes256.key}")
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
        println("key: $key")
        require(key.length == KEY_SIZE) { "AES-256 알고리즘 사용 시 32바이트 키가 필수입니다." }
        secretKey = SecretKeySpec(key.toByteArray(), ALGORITHM)
    }

    fun encrypt(plainText: String): String =
        try {
            val iv = ByteArray(IV_SIZE)
            SecureRandom().nextBytes(iv)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
            val encrypted = cipher.doFinal(plainText.toByteArray())
            val ivAndEncrypted = iv + encrypted
            Base64.getEncoder().encodeToString(ivAndEncrypted)
        } catch (e: Exception) {
            throw RuntimeException("AES encryption error", e)
        }

    fun decrypt(cipherText: String): String =
        try {
            val ivAndEncrypted = Base64.getDecoder().decode(cipherText)
            val iv = ivAndEncrypted.copyOfRange(0, IV_SIZE)
            val encrypted = ivAndEncrypted.copyOfRange(IV_SIZE, ivAndEncrypted.size)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
            val decrypted = cipher.doFinal(encrypted)
            String(decrypted)
        } catch (e: Exception) {
            throw RuntimeException("AES decryption error", e)
        }
}
