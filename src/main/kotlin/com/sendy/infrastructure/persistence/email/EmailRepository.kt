package com.sendy.infrastructure.persistence.email

import com.sendy.domain.email.EmailEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailRepository : JpaRepository<EmailEntity, Long> {
    fun save(entity: EmailEntity): EmailEntity

    fun findByEmail(email: String): EmailEntity?

    fun findByUserId(UserId: Long): EmailEntity?

    fun deleteByEmail(emailEntity: EmailEntity): EmailEntity?


}
