package com.sendy.sendyLegacyApi.infrastructure.persistence.user // package com.sendy.infrastructure.persistence.user

import com.sendy.sendyLegacyApi.domain.auth.UserEntityRepository
import com.sendy.sendyLegacyApi.domain.auth.UserRepository
import com.sendy.sendyLegacyApi.domain.user.UserEntity
import org.springframework.stereotype.Repository
import java.util.*

/**
 * UserRepository 인터페이스의 구현체
 * UserEntity와 User 도메인 모델 간의 변환을 담당
 */
@Repository
class UserRepositoryImpl(
    private val userEntityRepository: UserEntityRepository,
) : UserRepository {
    override fun findById(id: Long): Optional<UserEntity> =
        userEntityRepository
            .findById(id)

    override fun findByEmail(email: String): Optional<UserEntity> =
        userEntityRepository
            .findByEmail(email)

    override fun findByPhoneNumber(phoneNumber: String): Optional<UserEntity> =
        userEntityRepository
            .findByPhoneNumber(phoneNumber)

    override fun findActiveById(id: Long): Optional<UserEntity> =
        userEntityRepository
            .findByIdAndIsDeleteFalse(id)

    override fun findActiveByEmail(email: String): Optional<UserEntity> =
        userEntityRepository
            .findByEmail(email)
            .filter { !it.isDelete }

    override fun findActiveByPhoneNumber(phoneNumber: String): Optional<UserEntity> =
        userEntityRepository
            .findByPhoneNumberAndIsDeleteFalse(phoneNumber)

    override fun save(user: UserEntity): UserEntity = userEntityRepository.save(user)

    override fun existsByEmail(email: String): Boolean = userEntityRepository.existsByEmail(email)

    override fun existsByPhoneNumber(phoneNumber: String): Boolean = userEntityRepository.existsByPhoneNumber(phoneNumber)

    override fun existsActiveByEmail(email: String): Boolean = userEntityRepository.existsByEmailAndIsDeleteFalse(email)

    override fun existsActiveByPhoneNumber(phoneNumber: String): Boolean =
        userEntityRepository.existsByPhoneNumberAndIsDeleteFalse(phoneNumber)

//    /**
//     * UserEntity를 User 도메인 모델로 변환
//     */
//    private fun UserEntity.toUser(): User =
//        User(
//            id = this.id,
//            email = this.email,
//            password = this.password,
//            name = this.name,
//            phoneNumber = this.phoneNumber,
//            isDelete = this.isDelete,
//            emailVerified = this.emailVerified,
//            createdAt = this.createAt,
//            updatedAt = this.updateAt ?: this.createAt,
//        )
//
//    /**
//     * User 도메인 모델을 UserEntity로 변환
//     */
//    private fun User.toUserEntity(): UserEntity =
//        UserEntity(
//            id = this.id,
//            name = this.name,
//            phoneNumber = this.phoneNumber,
//            password = this.password,
//            email = this.email,
//            ci = null, // User 도메인 모델에는 ci 필드가 없으므로 null
//            birth = "", // User 도메인 모델에는 birth 필드가 없으므로 기본값
//            isDelete = this.isDelete,
//            createAt = this.createdAt,
//            updateAt = this.updatedAt,
//            deleteAt = if (this.isDelete) this.updatedAt else null,
//            emailVerified = this.emailVerified,
//        )
}
