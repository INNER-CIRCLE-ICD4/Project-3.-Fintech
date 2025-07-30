package com.sendy.application.usecase.account.command

import com.sendy.domain.account.AccountRepository
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class CreateAccountNumberImpl(
    private val accountRepository: AccountRepository
) : CreateAccountNumberUseCase{

    override fun generate():String {
        var accountNumber = generateAccountNumber()

        while(accountRepository.existsByAccountNumber(accountNumber)){
            accountNumber = generateAccountNumber()
        }
        return accountNumber
    }

    private fun generateAccountNumber():String {
        var random = Random.Default

        //예시 앞 3자리 기관코드 321-xxxx-xx-xxxx [기관코드는 erdcloud에 예시로가져왔습니다.)
        val prefix ="321"
        val body = (1..11).map{('0'..'9').random() }.joinToString("")

        return prefix + random
    }
}