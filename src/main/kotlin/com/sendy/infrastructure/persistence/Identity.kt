package com.sendy.infrastructure.persistence

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import org.springframework.data.domain.Persistable
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class Identity(
    @Id @Column private val id: Long,
) : Persistable<Long> {
    @Suppress("ktlint:standard:backing-property-naming")
    @Transient
    private var _isNew = true

    override fun getId(): Long = id

    override fun isNew(): Boolean = _isNew

    @PostLoad
    @PrePersist
    fun markNotNow() {
        _isNew = false
    }
}
