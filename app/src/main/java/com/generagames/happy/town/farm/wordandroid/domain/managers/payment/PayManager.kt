package com.generagames.happy.town.farm.wordandroid.domain.managers.payment

import com.generagames.happy.town.farm.wordandroid.domain.models.data.CardPay
import com.generagames.happy.town.farm.wordandroid.domain.models.data.GooglePay
import com.generagames.happy.town.farm.wordandroid.domain.models.data.PayResult
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost
import com.generagames.happy.town.farm.wordandroid.domain.models.data.WaitExpirationDateResult

interface PayManager {

    suspend fun pay(card: CardPay): PayResult

    suspend fun pay(card: GooglePay): WaitExpirationDateResult

    suspend fun getCosts(): List<SubCost>

    suspend fun waitExpirationDate(dateCacheId: String): WaitExpirationDateResult
}