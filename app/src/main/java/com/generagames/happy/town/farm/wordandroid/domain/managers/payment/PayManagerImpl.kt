package com.generagames.happy.town.farm.wordandroid.domain.managers.payment

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.enums.Currency
import can.lucky.of.core.utils.toPair
import com.generagames.happy.town.farm.wordandroid.net.clients.payment.PayClient
import com.generagames.happy.town.farm.wordandroid.domain.models.data.CardPay
import com.generagames.happy.town.farm.wordandroid.domain.models.data.PayResult
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost
import com.generagames.happy.town.farm.wordandroid.domain.models.data.WaitExpirationDateResult
import com.generagames.happy.town.farm.wordandroid.net.models.requests.CardPayRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PayManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val payClient: PayClient
) : PayManager {
    private val platform = "ANDROID"
    private val bearerToken : String
        get() = userCacheManager.toPair().second

    override suspend fun payCard(card: CardPay): PayResult {
        return withContext(Dispatchers.IO){

            return@withContext try {
                val respond = payClient.payCard(bearerToken, card.toCardPayRequest())

                PayResult(dateCacheId = respond.dateCacheId)
            } catch (e: Exception){
                PayResult(error = e.message)
            }
        }
    }

    override suspend fun getCosts(): List<SubCost> {
        return withContext(Dispatchers.IO){
            payClient.getCosts(bearerToken,platform).map {
                SubCost(
                    cost = it.cost,
                    days = it.days,
                    originalCost = it.originalCost,
                    currency = Currency.valueOf(it.currency)
                )
            }
        }
    }

    override suspend fun waitExpirationDate(dateCacheId: String): WaitExpirationDateResult {
        return withContext(Dispatchers.IO){
            return@withContext try {
                val respond = payClient.waitExpirationDate(bearerToken, dateCacheId)

                return@withContext WaitExpirationDateResult(
                    expirationDate = respond.expirationDate
                )
            } catch (e: Exception){
                WaitExpirationDateResult(errorMessage = e.message)
            }
        }
    }

    private fun CardPay.toCardPayRequest(): CardPayRequest {
        return CardPayRequest(
            cardNumber = this.cardNumber,
            cardName = this.cardName,
            expiryDate = this.expiryDate,
            cvv2 = this.cvv2,
            cost = this.cost,
            email = this.email,
            phoneNumber = this.phoneNumber
        )
    }
}