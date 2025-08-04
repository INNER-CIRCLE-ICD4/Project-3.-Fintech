package com.sendy.application.usecase.account

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountStatus
import com.sendy.support.util.getTsid
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class CreateAccountEntityService {
    fun execute(
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