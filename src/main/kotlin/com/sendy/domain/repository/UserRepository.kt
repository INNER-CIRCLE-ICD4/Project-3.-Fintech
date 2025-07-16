package com.sendy.domain.repository

import com.sendy.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


/**
 *
 * repository
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}