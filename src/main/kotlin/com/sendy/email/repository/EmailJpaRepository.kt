package com.sendy.email.repository

import com.sendy.email.model.Email
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EmailJpaRepository : JpaRepository<Email, Long>{
    fun save(entity : Email): Email

    fun findByEmail(email: String): Email?

    fun deleteByEmail(email : Email): Email?
}


