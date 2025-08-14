package com.sendy.sendyLegacyApi.domain.transfer

import org.springframework.data.jpa.repository.JpaRepository

interface TransferRepository : JpaRepository<TransferEntity, Long>
