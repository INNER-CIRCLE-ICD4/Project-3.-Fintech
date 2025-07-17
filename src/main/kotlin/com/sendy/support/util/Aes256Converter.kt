package com.sendy.support.util

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * packageName    : com.sendy.support.util
 * fileName       : Aes256Converter
 * author         : okdori
 * date           : 2025. 7. 17.
 * description    :
 */
@Component
@Converter
class Aes256Converter(
    @Value("\${aes256.key}") private val key: String
) : AttributeConverter<String, String> {
    private val aesUtil = Aes256Util(key)

    override fun convertToDatabaseColumn(attribute: String?): String? {
        return attribute?.let { aesUtil.encrypt(it) }
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        return dbData?.let { aesUtil.decrypt(it) }
    }
}
