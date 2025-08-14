package com.sendy.sendyLegacyApi.application.usecase.transfer.command

import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyCommand
import com.sendy.sendyLegacyApi.application.dto.transfer.TransferMoneyResponse

interface TransferMoneyUseCase {
    fun transferMoney(command: TransferMoneyCommand): TransferMoneyResponse
}
