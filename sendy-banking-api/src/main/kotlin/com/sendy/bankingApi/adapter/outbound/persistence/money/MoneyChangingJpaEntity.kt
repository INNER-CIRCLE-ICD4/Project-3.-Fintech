package com.sendy.bankingApi.adapter.outbound.persistence.money

import com.sendy.bankingApi.adapter.outbound.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "money_changing")
class MoneyChangingJpaEntity(
    id: String,
    @Column
    val targetUserId: Long,
    @Column
    val moneyChangingType: Int,
    @Column
    var changingMoneyStatus: Int, // 0: 요청, 1: 성공, 2: 실패
    @Column
    val moneyAmount: Long,
    @Column
    val createdAt: LocalDateTime = LocalDateTime.now(),
) : Identity(id)
