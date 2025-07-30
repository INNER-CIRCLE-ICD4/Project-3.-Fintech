package com.sendy.application.usecase.transfer.command

import com.sendy.application.dto.transfer.TransferMoneyCommand
import com.sendy.application.dto.transfer.TransferMoneyResponse

interface TransferMoneyUseCase {
    fun transferMoney(command: TransferMoneyCommand): TransferMoneyResponse
}
