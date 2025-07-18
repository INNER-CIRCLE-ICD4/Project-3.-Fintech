package com.sendy.support.util

object MaskingUtil {
    fun maskName(name: String): String {
        if (name.isEmpty()) return ""
        if (name.length == 1) return name
        if (name.length == 2) return name.first() + "*"
        return name.first() + "*".repeat(name.length - 2) + name.last()
    }

    fun maskPhone(phone: String): String {
        // 010-1234-5678 형식 기준, 가운데 4자리 마스킹
        val regex = Regex("(\\d{3})-(\\d{4})-(\\d{4})")
        return phone.replace(regex, "$1-****-$3")
    }

    fun maskAccount(account: String): String {
        if (account.length <= 4) return account
        return "*".repeat(account.length - 4) + account.takeLast(4)
    }
}
