package com.sendy.domain.transfer

interface TransferRepository {
    fun save(domain: Transfer)

    fun getTransferById(id: Long): Transfer
}
