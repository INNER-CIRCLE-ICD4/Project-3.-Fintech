package com.sendy.infrastructure.persistence.transfer

import com.sendy.infrastructure.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

// TODO: account 연관관계 설정
@Entity
@Table(name = "transfer")
class TransferJpaEntity(
    id: String,
    @Column
    var amount: Long,
    @Enumerated(EnumType.STRING)
    var status: TransferStatusEnum,
    @Column(nullable = true)
    val scheduledAt: LocalDateTime? = null,
    @Column
    val requestedAt: LocalDateTime,
    @Column(nullable = true)
    var completedAt: LocalDateTime? = null,
    @Column(nullable = true)
    val reason: String? = null,
) : Identity(id) {
    enum class TransferStatusEnum {
        PENDING,
        SUCCESS,
        FAILED,
        CANCELLED,
    }
}
