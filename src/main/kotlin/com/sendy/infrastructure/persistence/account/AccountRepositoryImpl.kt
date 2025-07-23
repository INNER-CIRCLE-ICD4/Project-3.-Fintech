package com.sendy.infrastructure.persistence.account

import com.sendy.domain.account.Account
import com.sendy.domain.account.AccountRepository
import org.apache.coyote.BadRequestException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class AccountRepositoryImpl(
    private val accountJpaRepository: AccountJpaRepository
) : AccountRepository {
    override fun save(account: Account) = accountJpaRepository.save(AccountEntity.from(account)).toModel()

    override fun findById(id: Long) = accountJpaRepository.findByIdOrNull(id)?.toModel()
        ?: throw BadRequestException("찾을 수 없는 계좌입니다. id: $id")

    override fun findByUserId(userId: Long) = accountJpaRepository.findByUserId(userId).map { it.toModel() }
}
