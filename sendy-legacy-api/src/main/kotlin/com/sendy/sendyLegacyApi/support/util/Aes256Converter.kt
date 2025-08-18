package com.sendy.sendyLegacyApi.support.util

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class Aes256Converter(
    private val aes256Util: Aes256Util,
) : AttributeConverter<String, String> {
    override fun convertToDatabaseColumn(attribute: String?): String? = attribute?.let { aes256Util.encrypt(it) }

    override fun convertToEntityAttribute(dbData: String?): String? = dbData?.let { aes256Util.decrypt(it) }
}
