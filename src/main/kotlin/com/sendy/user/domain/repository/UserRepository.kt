//package com.sendy.user.domain.repository
//
//import com.sendy.user.domain.model.UserEntity
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.stereotype.Repository
//
//
///**
// *
// * repository
// */
//@Repository
//interface UserRepository : JpaRepository<UserEntity, Long> , UserCustomRepository {
//    fun findByEmail(email: String): UserEntity?
//
//    fun save(entity : UserEntity): UserEntity
//
//    fun findByUserId(id: Long): UserEntity?
//
//    fun findByEmailAndPassword(email: String , password: String): UserEntity?
//}