package com.sendy.sendyLegacyApi.application.dto.transfer

import com.sendy.sendyLegacyApi.domain.enum.TransferStatusEnum

data class TransferMoneyResponse(
    val transferStatus: TransferStatusEnum,
)
