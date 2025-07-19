package com.sendy.application.usecase.transfer.command

import com.sendy.application.dto.transfer.TransferMoneyCommand

interface TransferMoneyUseCase {
    fun transferMoney(command: TransferMoneyCommand)
}
