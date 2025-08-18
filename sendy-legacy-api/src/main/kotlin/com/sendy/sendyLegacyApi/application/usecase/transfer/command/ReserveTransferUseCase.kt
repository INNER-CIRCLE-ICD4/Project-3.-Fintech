package com.sendy.sendyLegacyApi.application.usecase.transfer.command

import com.sendy.sendyLegacyApi.application.dto.transfer.ReserveTransferCommand
import com.sendy.sendyLegacyApi.application.dto.transfer.TransferId

interface ReserveTransferUseCase {
    fun reserveTransfer(command: ReserveTransferCommand): TransferId
}
