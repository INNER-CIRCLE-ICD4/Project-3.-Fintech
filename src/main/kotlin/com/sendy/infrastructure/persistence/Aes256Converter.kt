package com.sendy.infrastructure.persistence

import com.sendy.support.util.Aes256Util
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
@Converter
class Aes256Converter(
    @Value("\${aes256.key}") private val key: String,
) : AttributeConverter<String, String> {
    private val aesUtil = Aes256Util(key)

    override fun convertToDatabaseColumn(attribute: String?): String? = attribute?.let { aesUtil.encrypt(it) }

    override fun convertToEntityAttribute(dbData: String?): String? = dbData?.let { aesUtil.decrypt(it) }
}
