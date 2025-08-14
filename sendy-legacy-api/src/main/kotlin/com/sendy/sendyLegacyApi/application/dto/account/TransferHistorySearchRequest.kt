package com.sendy.sendyLegacyApi.application.dto.account

import java.time.LocalDateTime

data class TransferHistorySearchRequest(
    val userId: Long,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val sortOrder: String = "desc" // "asc" 또는 "desc"
)
