package com.sendy.infrastructure.persistence.account

import org.springframework.data.jpa.repository.JpaRepository

interface AccountJpaRepository : JpaRepository<AccountEntity, Long> {
    fun findByUserId(userId: Long): List<AccountEntity>
}
