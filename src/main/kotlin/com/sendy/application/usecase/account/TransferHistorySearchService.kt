package com.sendy.application.usecase.account

import com.sendy.application.dto.account.TransferHistorySearchRequest
import com.sendy.application.dto.account.TransferHistorySearchResponse
import com.sendy.application.usecase.account.command.TransferHistorySearchUseCase
import com.sendy.domain.account.TransactionHistoryEntity
import com.sendy.domain.account.TransactionHistoryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class TransferHistorySearchService(
    private val transactionHistoryRepository: TransactionHistoryRepository
) : TransferHistorySearchUseCase {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    override fun execute(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime): TransferHistorySearchResponse {
        // 기본값으로 desc 정렬 사용하여 전체 거래 내역을 조회하고, 첫 번째 결과만 반환
        val histories = findAllTransactionHistory(userId, startDate, endDate, "desc")
        return histories.firstOrNull() ?: throw IllegalStateException("이체 내역이 존재하지 않습니다.")
    }

    override fun executeList(request: TransferHistorySearchRequest): List<TransferHistorySearchResponse> {
        return findAllTransactionHistory(request.userId, request.startDate, request.endDate, request.sortOrder)
    }

    /**
     * 이체 내역을 검색합니다.
     * @param request 검색 요청 정보 (userId, startDate, endDate, sortOrder)
     * @return 검색된 이체 내역 리스트
     */
    fun searchTransferHistory(request: TransferHistorySearchRequest): List<TransferHistorySearchResponse> {
        return executeList(request)
    }

    /**
     * 입금 내역을 검색합니다.
     * @param userId 사용자 ID
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @param sortOrder 정렬 순서 ("asc" 또는 "desc")
     * @return 입금 내역 리스트
     */
    override fun findDepositHistory(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime, sortOrder: String): List<TransferHistorySearchResponse> {
        validateSortOrder(sortOrder)
        val depositHistories = transactionHistoryRepository.findDepositHistory(startDate, endDate, sortOrder)
        return depositHistories.map { convertToResponse(userId, it) }
    }

    /**
     * 출금 내역을 검색합니다.
     * @return 출금 내역 리스트
     */
    override fun findWithdrawHistory(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime, sortOrder: String): List<TransferHistorySearchResponse> {
        validateSortOrder(sortOrder)
        val withdrawHistories = transactionHistoryRepository.findWithDrawHistory(startDate, endDate, sortOrder)
        return withdrawHistories.map { convertToResponse(userId, it) }
    }

    /**
     * 입출금 전체 검색
     */
    override fun findAllTransactionHistory(userId: Long, startDate: LocalDateTime, endDate: LocalDateTime, sortOrder: String): List<TransferHistorySearchResponse> {
        validateSortOrder(sortOrder)
        val allTransactionHistories = transactionHistoryRepository.findAllTransactionHistory(startDate, endDate, sortOrder)
        return allTransactionHistories.map { convertToResponse(userId, it) }
    }

    /**
     * TransactionHistoryEntity를 TransferHistorySearchResponse로 변환합니다.
     */
    private fun convertToResponse(userId: Long, entity: TransactionHistoryEntity): TransferHistorySearchResponse {
        return TransferHistorySearchResponse(
            userId = userId,
            tx_type = entity.type.name,
            amount = entity.amount,
            balance_after = entity.balanceAfter,
            description = entity.description ?: "",
            created_at = entity.createdAt.format(dateFormatter)
        )
    }

    /**
     * 정렬 순서 파라미터의 유효성을 검증합니다.
     */
    private fun validateSortOrder(sortOrder: String) {
        if (sortOrder !in listOf("asc", "desc")) {
            throw IllegalArgumentException("정렬 순서는 'asc' 또는 'desc'만 허용됩니다. 입력값: $sortOrder")
        }
    }
}