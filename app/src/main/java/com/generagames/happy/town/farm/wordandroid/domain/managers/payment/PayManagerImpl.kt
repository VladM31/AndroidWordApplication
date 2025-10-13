package com.generagames.happy.town.farm.wordandroid.domain.managers.payment

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.enums.Currency
import can.lucky.of.core.utils.getRespondMessage
import can.lucky.of.core.utils.toPair
import com.generagames.happy.town.farm.wordandroid.domain.models.data.CardPay
import com.generagames.happy.town.farm.wordandroid.domain.models.data.GooglePay
import com.generagames.happy.town.farm.wordandroid.domain.models.data.PayResult
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost
import com.generagames.happy.town.farm.wordandroid.domain.models.data.WaitExpirationDateResult
import com.generagames.happy.town.farm.wordandroid.domain.models.keys.Platforms
import com.generagames.happy.town.farm.wordandroid.net.clients.payment.PayClient
import com.generagames.happy.town.farm.wordandroid.net.models.requests.CardPayRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.pay.GooglePayRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PayManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val payClient: PayClient
) : PayManager {
    private val bearerToken : String
        get() = userCacheManager.toPair().second

    override suspend fun pay(card: CardPay): PayResult {
        return withContext(Dispatchers.IO){

            return@withContext try {
                val respond = payClient.pay(bearerToken, card.toCardPayRequest())

                PayResult(dateCacheId = respond.dateCacheId)
            } catch (e: Exception){
                PayResult(error = e.message)
            }
        }
    }

    override suspend fun pay(payData: GooglePay): WaitExpirationDateResult {
        return withContext(Dispatchers.IO){
            return@withContext try {
                val respond = payClient.pay(bearerToken, payData.toResponse())

                WaitExpirationDateResult(expirationDate = respond.expirationDate)
            } catch (e: Exception){
                WaitExpirationDateResult(errorMessage = e.message)
            }
        }
    }

    override suspend fun getCosts(): List<SubCost> {

        return withContext(Dispatchers.IO){
            payClient.getCosts(bearerToken,Platforms.PLATFORM_ANDROID).map {
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

                WaitExpirationDateResult(errorMessage = e.getRespondMessage())
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

    private fun GooglePay.toResponse(): GooglePayRequest {
        return GooglePayRequest(
            token = this.token,
            currency = this.currency,
            cost = this.cost,
            usdCost = this.usdCost
        )
    }
}