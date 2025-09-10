package com.sendy.bankingApi.domain.money

import com.sendy.bankingApi.domain.vo.UserId
import java.time.LocalDateTime

data class RequestMoneyChanging(
    val id: String,
    val targetUserId: UserId, // 어떤 유저가 증/감액 요청을 했는지
    // 요청이 증액인지 감액인지
    val changingType: Int, // 0: 증액, 1: 감액
    // 증액/ 감액의 금액
    val changingMoneyAmount: Long,
    // 머니 증/감액 요청에 대한 상태
    val changingMoneyStatus: Int, // 0: 요청, 1: 성공, 2: 실패, 3: 취소
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
