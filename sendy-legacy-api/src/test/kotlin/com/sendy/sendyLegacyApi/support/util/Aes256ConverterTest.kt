package com.sendy.sendyLegacyApi.support.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class Aes256ConverterTest {
    private val key = "12345678901234567890123456789012"
    private val converter = Aes256Converter(key)

    @Test
    fun `DB 저장시 암호화, 조회시 복호화가 정상 동작해야 한다`() {
        val original = "테스트 문자열_123-!@#"
        val encrypted = converter.convertToDatabaseColumn(original)
        val decrypted = converter.convertToEntityAttribute(encrypted)
        assertEquals(original, decrypted)
    }

    @Test
    fun `null 값은 그대로 반환해야 한다`() {
        assertNull(converter.convertToDatabaseColumn(null))
        assertNull(converter.convertToEntityAttribute(null))
    }
}
