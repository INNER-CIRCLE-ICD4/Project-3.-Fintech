package com.sendy.bankingApi.adapter.outbound.persistence.bankAccount

import org.springframework.data.jpa.repository.JpaRepository

interface BankAccountJpaRepository : JpaRepository<BankAccountJpaEntity, String>
