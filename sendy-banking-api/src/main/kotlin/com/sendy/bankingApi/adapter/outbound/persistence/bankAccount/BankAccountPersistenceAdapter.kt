package com.sendy.bankingApi.adapter.outbound.persistence.bankAccount

import com.sendy.bankingApi.application.outboud.bankAccount.RegisterBankAccountOutPort
import com.sendy.bankingApi.domain.bankAccount.RegisterBankAccount
import org.springframework.stereotype.Component

@Component
class BankAccountPersistenceAdapter(
    private val bankAccountJpaRepository: BankAccountJpaRepository,
) : RegisterBankAccountOutPort {
    override fun createRegisterBankAccount(registerBankAccount: RegisterBankAccount): String {
        val entity =
            bankAccountJpaRepository.save(
                BankAccountJpaEntity(
                    id = registerBankAccount.id,
                    userId = registerBankAccount.userId.value,
                    bankName = registerBankAccount.bankName,
                    bankAccountNumber = registerBankAccount.bankAccountNumber,
                    linkedStatusIsValid = registerBankAccount.linkedStatusIsValid,
                ),
            )

        return entity.id
    }
}
