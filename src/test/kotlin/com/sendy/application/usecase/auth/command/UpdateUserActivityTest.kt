package com.sendy.application.usecase.auth.command

import com.sendy.application.usecase.auth.interfaces.UpdateUserActivity
import com.sendy.domain.auth.UserRepository
import com.sendy.domain.model.User
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import java.time.LocalDateTime
import kotlin.test.*

class UpdateUserActivityTest {

    private lateinit var userRepository: UserRepository
    private lateinit var updateUserActivity: UpdateUserActivity

    @BeforeEach
    fun setUp() {
        userRepository = mockk()
        updateUserActivity = UpdateUserActivityImpl(userRepository)
    }

    @Test
    fun `사용자 활동 시간 업데이트 테스트`() {
        // Given
        val originalUpdatedAt = LocalDateTime.of(2023, 1, 1, 12, 0, 0)
        val user = User(
            id = 1L,
            email = "test@example.com",
            password = "hashedPassword",
            name = "테스트 사용자",
            phoneNumber = "01012345678",
            emailVerified = true,
            isDelete = false,
            updatedAt = originalUpdatedAt
        )

        val updatedUser = user.updateLastActivity()

        every { userRepository.save(any()) } returns updatedUser

        // When
        val result = updateUserActivity.execute(user)

        // Then
        assertNotNull(result)
        assertEquals(user.id, result.id)
        assertEquals(user.email, result.email)
        assertTrue(result.updatedAt.isAfter(originalUpdatedAt))

        verify(exactly = 1) { userRepository.save(any()) }
        confirmVerified(userRepository)
    }

    @Test
    fun `업데이트된 사용자 객체가 반환되는지 테스트`() {
        // Given
        val user = User(
            id = 1L,
            email = "test@example.com",
            password = "hashedPassword",
            name = "테스트 사용자",
            phoneNumber = "01012345678",
            emailVerified = true,
            isDelete = false
        )

        val updatedUser = user.updateLastActivity()

        every { userRepository.save(any()) } returns updatedUser

        // When
        val result = updateUserActivity.execute(user)

        // Then
        assertNotNull(result)
        assertNotEquals(user.updatedAt, result.updatedAt)
        assertTrue(result.updatedAt.isAfter(user.updatedAt))

        verify(exactly = 1) { userRepository.save(any()) }
        confirmVerified(userRepository)
    }

    @Test
    fun `Repository save 메서드가 정확히 한 번 호출되는지 테스트`() {
        // Given
        val user = User(
            id = 1L,
            email = "test@example.com",
            password = "hashedPassword",
            name = "테스트 사용자",
            phoneNumber = "01012345678",
            emailVerified = true,
            isDelete = false
        )

        val updatedUser = user.updateLastActivity()
        every { userRepository.save(any()) } returns updatedUser

        // When
        updateUserActivity.execute(user)

        // Then
        verify(exactly = 1) { userRepository.save(any()) }
        confirmVerified(userRepository)
    }
}
