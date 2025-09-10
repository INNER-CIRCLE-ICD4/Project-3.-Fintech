package com.sendy.bankingApi.application.outboud.money

import com.sendy.bankingApi.domain.money.RequestMoneyChanging

interface IncreaseMoneyChangingOutPort {
    fun increaseMoney(requestMoneyChanging: RequestMoneyChanging): RequestMoneyChanging
}
