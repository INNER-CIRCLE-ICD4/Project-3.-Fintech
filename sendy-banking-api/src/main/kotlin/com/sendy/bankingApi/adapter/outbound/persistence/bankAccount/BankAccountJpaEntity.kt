package com.sendy.bankingApi.adapter.outbound.persistence.bankAccount

import com.sendy.bankingApi.adapter.outbound.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "bank_account")
class BankAccountJpaEntity(
    id: String,
    @Column
    val userId: Long,
    @Column
    val bankName: String,
    @Column
    val bankAccountNumber: String,
    @Column
    val linkedStatusIsValid: Boolean,
) : Identity(id)
