package com.sendy.user.domain.repository

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.stereotype.Repository


/**
 *
 * repository
 */
@Repository
class UserCustomRepositoryImpl(
    @PersistenceContext
    private val em: EntityManager
) : UserCustomRepository {


}