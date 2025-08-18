package com.sendy.sendyLegacyApi.domain.transfer

import com.sendy.sendyLegacyApi.domain.enum.TransferStatusEnum
import com.sendy.sendyLegacyApi.infrastructure.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "transfer")
class TransferEntity(
    id: Long,
    @Column
    val sendUserId: Long,
    @Column
    val sendAccountNumber: String,
    @Column(nullable = true)
    var receivePhoneNumber: String? = null,
    @Column(nullable = true)
    var receiveAccountNumber: String? = null,
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
    fun changeToSuccess() {
        status = TransferStatusEnum.SUCCESS
    }

    fun changeToFail() {
        status = TransferStatusEnum.FAILED
    }
}
