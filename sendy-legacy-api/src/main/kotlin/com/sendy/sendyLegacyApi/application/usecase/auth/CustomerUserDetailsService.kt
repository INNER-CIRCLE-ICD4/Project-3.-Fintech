package com.sendy.sendyLegacyApi.application.usecase.auth

import com.sendy.sendyLegacyApi.domain.auth.UserEntityRepository
import com.sendy.sendyLegacyApi.infrastructure.persistence.email.EmailRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomerUserDetailsService(
//    private val userEntityRepository: UserRepository,
    private val userEntityRepository: UserEntityRepository,
    private val email: EmailRepository,
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails {
        // JWT 인증 시에는 userId가 문자열로 전달됨
        val userEntity =
            try {
                val userId = username!!.toLong()
                userEntityRepository
                    .findById(userId)
                    .orElseThrow { UsernameNotFoundException("해당 사용자가 없습니다: $username") }
            } catch (e: NumberFormatException) {
                // 일반 로그인 시에는 email로 조회
                userEntityRepository.findByEmail(username!!) ?: throw UsernameNotFoundException("해당 사용자가 없습니다: $username")
            }

        return org.springframework.security.core.userdetails.User(
            userEntity.email,
            userEntity.password,
            listOf(), // 권한 등 추가 필요시 여기서 설정
        )
    }
}
