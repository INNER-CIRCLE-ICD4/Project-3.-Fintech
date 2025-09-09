package com.sendy.bankingApi.adapter.outbound.persistence

import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import org.springframework.data.domain.Persistable

@MappedSuperclass
abstract class Identity(
    @Id
    private val id: String,
) : Persistable<String> {
    @Suppress("ktlint:standard:backing-property-naming")
    @Transient
    private var _isNew = true

    override fun getId(): String = id

    override fun isNew(): Boolean = _isNew

    @PostLoad
    @PrePersist
    fun markNotNow() {
        _isNew = false
    }
}
