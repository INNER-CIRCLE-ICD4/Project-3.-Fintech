package com.sendy.bankingApi.adapter.inbound.rest.money.dto

data class MoneyChangingResultDetail(
    val id: String,
    val moneyChangingType: MoneyChangingType, // 0: 증액, 1: 감액
    val moneyChangingResultStatus: MoneyChangingResultStatus,
    val amount: Long,
) {
    enum class MoneyChangingType {
        INCREASING,
        DECREASING,
    }

    enum class MoneyChangingResultStatus {
        SUCCESS,
        FAILED,
        FAILED_NOT_ENOUGH_MONEY,
        FAILED_NOT_USER,
        FAILED_NOT_CHANGING,
    }
}
