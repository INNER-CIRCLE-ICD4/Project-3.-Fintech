package com.sendy.domain.account

import com.sendy.infrastructure.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "account")
class AccountEntity(
    id: Long,

    @Column(name = "account_number", nullable = false, length = 13)
    val accountNumber: String,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    val status: AccountStatus,

    @Column(name = "is_primary", nullable = false)
    val isPrimary: Boolean,

    @Column(name = "is_limited_account", nullable = false)
    val isLimitedAccount: Boolean,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    val updatedAt: LocalDateTime,

    @Column(name = "balance", nullable = false)
    val balance: Long
) : Identity(id)
