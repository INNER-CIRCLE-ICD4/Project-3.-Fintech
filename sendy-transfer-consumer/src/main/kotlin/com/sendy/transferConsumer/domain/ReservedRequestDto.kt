package com.sendy.transferConsumer.domain

import com.sendy.transferDomain.domain.vo.TransferId

data class ReservedRequestDto(
    val transferIds: List<TransferId>,
)
