package com.sendy.application.dto.transfer

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length
import java.time.LocalDateTime

data class TransferMoneyCommand(
    val userId: Long,
    val senderAccountNumber: String,
    val receiveAccountNumber: String,
    val amount: Long,
    val requestedAt: LocalDateTime,
    @field:NotBlank(message = "비밀번호는 필수 값 입니다.")
    @field:Length(max = 4, min = 4, message = "비밀번호는 4자리여야됩니다.")
    val password: String,
)
