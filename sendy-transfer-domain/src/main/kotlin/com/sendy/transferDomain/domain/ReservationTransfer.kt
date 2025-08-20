package com.sendy.transferDomain.domain

import com.sendy.transferDomain.domain.vo.TransferId

data class ReservationTransfer(
    val totalCount: Int,
    val nextCursor: TransferId?,
    val transferIds: List<TransferId>,
)
