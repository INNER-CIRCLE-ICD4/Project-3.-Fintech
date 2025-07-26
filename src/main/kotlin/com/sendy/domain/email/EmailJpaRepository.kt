package com.sendy.domain.email

import com.sendy.domain.model.Email
import org.springframework.data.jpa.repository.JpaRepository

interface EmailJpaRepository : JpaRepository<Email, Long> {
    fun save(entity: Email): Email

    fun findByEmail(email: String): Email?

    fun deleteByEmail(email: Email): Email?
}
