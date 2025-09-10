package com.sendy.bankingApi.adapter.outbound.persistence.money

import org.springframework.data.jpa.repository.JpaRepository

interface MoneyChangingJpaRepository : JpaRepository<MoneyChangingJpaEntity, String>
