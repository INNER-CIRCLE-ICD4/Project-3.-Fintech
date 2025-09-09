package com.sendy.bankingApi.adapter.outbound.persistence.bankAccount

import com.sendy.bankingApi.adapter.outbound.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "firm_banking")
class FirmBankingJpaEntity(
    id: String,
    @Column
    val fromBankName: String,
    @Column
    val fromBankAccountNumber: String,
    @Column
    val toBankName: String,
    @Column
    val toBankAccountNumber: String,
    @Column
    val moneyAmount: Long,
    @Column
    var firmBankingStatus: Int,
) : Identity(id)
