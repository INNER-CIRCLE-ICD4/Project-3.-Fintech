package com.sendy.support.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class Aes256UtilTest {
    private val key = "12345678901234567890123456789012"
    private val aesUtil = Aes256Util(key)

    @Test
    fun `암호화 후 복호화하면 원본과 같아야 한다`() {
        val original = "테스트 문자열_123-!@#"
        val encrypted = aesUtil.encrypt(original)
        val decrypted = aesUtil.decrypt(encrypted)
        assertEquals(original, decrypted)
    }

    @Test
    fun `같은 평문은 암호문이 매번 달라진다`() {
        val text = "same text 123 !@#"
        val encrypted1 = aesUtil.encrypt(text)
        val encrypted2 = aesUtil.encrypt(text)
        assert(encrypted1 != encrypted2)
    }

    @Test
    fun `복호화에 잘못된 값이 들어오면 예외가 발생한다`() {
        assertThrows(RuntimeException::class.java) {
            aesUtil.decrypt("잘못된 값")
        }
    }
}
