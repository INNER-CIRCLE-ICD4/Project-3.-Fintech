package com.sendy.bankingApi.application.outboud.money

import com.sendy.bankingApi.domain.money.RequestMoneyChanging

interface MoneyChangingOutPort {
    fun updateMoneyChanging(requestMoneyChanging: RequestMoneyChanging)
}
