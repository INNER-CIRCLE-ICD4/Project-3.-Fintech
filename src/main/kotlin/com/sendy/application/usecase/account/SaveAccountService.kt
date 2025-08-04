package com.sendy.application.usecase.account

import com.sendy.application.usecase.account.command.SaveAccountUseCase
import com.sendy.domain.account.AccountEntity
import com.sendy.domain.account.AccountRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class SaveAccountService(
    private val accountRepository: AccountRepository,
):SaveAccountUseCase {
    override fun execute(accountEntity: AccountEntity): AccountEntity {
        return accountRepository.save(accountEntity)
    }
}