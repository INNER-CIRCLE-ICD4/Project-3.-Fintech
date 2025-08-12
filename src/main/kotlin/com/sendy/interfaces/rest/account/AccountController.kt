package com.sendy.interfaces.rest.account

import com.sendy.application.dto.account.CreateAccountRequest
import com.sendy.application.dto.account.CreateAccountResponse
import com.sendy.application.dto.account.TransferHistorySearchRequest
import com.sendy.application.usecase.account.command.CreateAccountUseCase
import com.sendy.application.usecase.account.command.TransferHistorySearchUseCase
import com.sendy.application.usecase.account.query.ReadAccountBalanceUseCase
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("accounts")
class AccountController(
    private val createAccountUseCase: CreateAccountUseCase,
    private val readAccountBalanceUseCase: ReadAccountBalanceUseCase,
    private val transferHistorySearchUseCase : TransferHistorySearchUseCase,
) {
    @PostMapping
    fun createAccount(
        @RequestBody request: CreateAccountRequest,
    ): CreateAccountResponse = createAccountUseCase.execute(request)

    @GetMapping("/balance")
    fun getAccountBalance(
        @RequestParam userId: Long,
        @RequestParam accountNumber: String,
    ) = readAccountBalanceUseCase.execute(userId, accountNumber)

    // 전체 거래 내역 조회 (정렬 옵션 포함)
    @PostMapping("/all_transaction_history")
    fun getAllTransactionHistory(
        @RequestBody request: TransferHistorySearchRequest
    ) = transferHistorySearchUseCase.executeList(request)

    // 입금 내역만 조회
    @GetMapping("/deposit_history")
    fun getDepositHistory(
        @RequestParam userId: Long,
        @RequestParam startDate: LocalDateTime,
        @RequestParam endDate: LocalDateTime,
        @RequestParam(defaultValue = "desc") sortOrder: String
    ) = transferHistorySearchUseCase.findDepositHistory(userId, startDate, endDate, sortOrder)

    // 출금 내역만 조회
    @GetMapping("/withdraw_history")
    fun getWithdrawHistory(
        @RequestParam userId: Long,
        @RequestParam startDate: LocalDateTime,
        @RequestParam endDate: LocalDateTime,
        @RequestParam(defaultValue = "desc") sortOrder: String
    ) = transferHistorySearchUseCase.findWithdrawHistory(userId, startDate, endDate, sortOrder)

    // 전체 거래 내역 조회 (GET 방식)
    @GetMapping("/all_history")
    fun getAllHistory(
        @RequestParam userId: Long,
        @RequestParam startDate: LocalDateTime,
        @RequestParam endDate: LocalDateTime,
        @RequestParam(defaultValue = "desc") sortOrder: String
    ) = transferHistorySearchUseCase.findAllTransactionHistory(userId, startDate, endDate, sortOrder)
}
