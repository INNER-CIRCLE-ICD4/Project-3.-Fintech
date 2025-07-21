package com.sendy.infrastructure.persistence.transfer

import com.sendy.domain.transfer.TransferLimit
import com.sendy.infrastructure.persistence.Identity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@Table(name = "transfer_limit")
@EntityListeners(AuditingEntityListener::class)
class TransferLimitJpaEntity(
    id: Long,
    @Column(columnDefinition = "VARCHAR(8)")
    val dailyDt: String,
    @Column
    val dailyLimit: Long,
    @Column
    val singleTransactionLimit: Long,
    @Column
    var dailyCount: Long,
    @CreationTimestamp
    var createdAt: LocalDateTime,
    @UpdateTimestamp
    var updatedAt: LocalDateTime,
    @Column
    var userId: Long,
) : Identity(id) {
    override fun toString(): String =
        """
        id: $id,
        isNew: $isNew
        """.trimIndent()

    fun updateFrom(domain: TransferLimit) {
        dailyCount = domain.dailyCount
    }
}
