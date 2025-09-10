package com.sendy.bankingApi.adapter.outbound.persistence.bankAccount

import com.sendy.bankingApi.application.outboud.bankAccount.BankAccountOutPort
import com.sendy.bankingApi.application.outboud.bankAccount.RegisterBankAccountOutPort
import com.sendy.bankingApi.domain.bankAccount.RegisterBankAccount
import com.sendy.bankingApi.domain.vo.UserId
import org.springframework.stereotype.Component

@Component
class BankAccountPersistenceAdapter(
    private val bankAccountJpaRepository: BankAccountJpaRepository,
) : RegisterBankAccountOutPort,
    BankAccountOutPort {
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

    override fun getBankAccountByUserId(userId: UserId): RegisterBankAccount =
        bankAccountJpaRepository
            .findByUserId(userId.value)
            ?.let {
                RegisterBankAccount(
                    id = it.id,
                    userId = UserId(it.userId),
                    bankName = it.bankName,
                    bankAccountNumber = it.bankAccountNumber,
                    linkedStatusIsValid = it.linkedStatusIsValid,
                )
            } ?: throw RuntimeException("bank account is not found")
}
