package com.sendy.domain.transfer

import org.springframework.data.jpa.repository.JpaRepository

interface TransferRepository : JpaRepository<TransferEntity, Long>
