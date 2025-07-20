package com.sendy.infrastructure.persistence

import com.sendy.domain.model.User
import com.sendy.domain.repository.UserRepository
import org.springframework.stereotype.Repository
import java.util.*

/**
 * UserRepository 인터페이스의 구현체
 * UserEntity와 User 도메인 모델 간의 변환을 담당
 */
@Repository
class UserRepositoryImpl(
    private val userEntityRepository: com.sendy.domain.repository.UserEntityRepository
) : UserRepository {
    
    override fun findById(id: Long): Optional<User> {
        return userEntityRepository.findById(id)
            .map { it.toUser() }
    }
    
    override fun findByEmail(email: String): Optional<User> {
        return userEntityRepository.findByEmail(email)
            .map { it.toUser() }
    }
    
    override fun findByPhoneNumber(phoneNumber: String): Optional<User> {
        return userEntityRepository.findByPhoneNumber(phoneNumber)
            .map { it.toUser() }
    }
    
    override fun findActiveById(id: Long): Optional<User> {
        return userEntityRepository.findByIdAndIsDeleteFalse(id)
            .map { it.toUser() }
    }
    
    override fun findActiveByEmail(email: String): Optional<User> {
        return userEntityRepository.findByEmail(email)
            .filter { !it.isDelete }
            .map { it.toUser() }
    }
    
    override fun findActiveByPhoneNumber(phoneNumber: String): Optional<User> {
        return userEntityRepository.findByPhoneNumberAndIsDeleteFalse(phoneNumber)
            .map { it.toUser() }
    }
    
    override fun save(user: User): User {
        val userEntity = user.toUserEntity()
        val savedEntity = userEntityRepository.save(userEntity)
        return savedEntity.toUser()
    }
    
    override fun existsByEmail(email: String): Boolean {
        return userEntityRepository.existsByEmail(email)
    }
    
    override fun existsByPhoneNumber(phoneNumber: String): Boolean {
        return userEntityRepository.existsByPhoneNumber(phoneNumber)
    }
    
    override fun existsActiveByEmail(email: String): Boolean {
        return userEntityRepository.existsByEmailAndIsDeleteFalse(email)
    }
    
    override fun existsActiveByPhoneNumber(phoneNumber: String): Boolean {
        return userEntityRepository.existsByPhoneNumberAndIsDeleteFalse(phoneNumber)
    }
    
    /**
     * UserEntity를 User 도메인 모델로 변환
     */
    private fun UserEntity.toUser(): User {
        return User(
            id = this.id,
            email = this.email,
            password = this.password,
            name = this.name,
            phoneNumber = this.phoneNumber,
            isDelete = this.isDelete,
            emailVerified = this.emailVerified,
            createdAt = this.createAt,
            updatedAt = this.updateAt ?: this.createAt
        )
    }
    
    /**
     * User 도메인 모델을 UserEntity로 변환
     */
    private fun User.toUserEntity(): UserEntity {
        return UserEntity(
            id = this.id,
            name = this.name,
            phoneNumber = this.phoneNumber,
            password = this.password,
            email = this.email,
            ci = null, // User 도메인 모델에는 ci 필드가 없으므로 null
            birth = "", // User 도메인 모델에는 birth 필드가 없으므로 기본값
            isDelete = this.isDelete,
            createAt = this.createdAt,
            updateAt = this.updatedAt,
            deleteAt = if (this.isDelete) this.updatedAt else null,
            emailVerified = this.emailVerified
        )
    }
} 