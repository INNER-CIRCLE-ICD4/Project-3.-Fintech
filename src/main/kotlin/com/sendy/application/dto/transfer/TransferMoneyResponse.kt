package com.sendy.application.dto.transfer

import com.sendy.domain.enum.TransferStatusEnum

data class TransferMoneyResponse(
    val transferStatus: TransferStatusEnum,
)
