package com.sendy.application.usecase.account

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.usecase.account.command.CreateAccountEntityUseCase
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountStatus
import com.sendy.support.util.getTsid
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CreateAccountEntityService : CreateAccountEntityUseCase {
    override fun execute(
        request: CreateAccountRequest,
        accountNumber: String,
        encryptedPassword: String
    ): AccountEntity {
        return AccountEntity(
            id = getTsid(),
            accountNumber = accountNumber,
            userId = request.userId,
            password = encryptedPassword,
            status = AccountStatus.ACTIVE,
            isPrimary = request.isPrimary,
            isLimitedAccount = request.isLimitedAccount,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            balance = 0L,
        )
    }
}