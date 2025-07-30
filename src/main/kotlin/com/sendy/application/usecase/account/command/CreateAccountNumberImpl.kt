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
        var part1 ="321"
        var part2 = random.nextInt(1000, 10000).toString().padStart(4, '0')
        var part3 = random.nextInt(10, 100).toString().padStart(2, '0')
        var part4 = random.nextInt(1000, 10000).toString().padStart(4, '0')

        return "$part1$part2$part3$part4"
    }
}