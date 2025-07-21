package com.sendy.domain.service

import com.sendy.user.domain.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("customerUserDetailsService")
class CustomerUserDetailsService(
    private val userEntityRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        // JWT 인증 시에는 userId가 문자열로 전달됨
        val user = try {
            val userId = username.toLong()
            userEntityRepository.findById(userId)
                .orElseThrow { UsernameNotFoundException("해당 사용자가 없습니다: $username") }
        } catch (e: NumberFormatException) {
            // 일반 로그인 시에는 email로 조회
            userEntityRepository.findByEmail(username)
                ?: throw UsernameNotFoundException("해당 사용자가 없습니다: $username")
        }
        
        return org.springframework.security.core.userdetails.User(
            user.email, user.password, listOf() // 권한 등 추가 필요시 여기서 설정
        )
    }
}