package com.sendy.sharedKafka.infrastructure

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import org.springframework.data.domain.Persistable

@MappedSuperclass
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
