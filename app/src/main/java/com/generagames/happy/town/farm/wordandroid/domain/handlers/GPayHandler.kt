package com.generagames.happy.town.farm.wordandroid.domain.handlers

import android.app.Activity
import com.google.android.gms.wallet.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

class GPayHandler(private val activity: Activity) {

    private val paymentsClient: PaymentsClient by lazy {
        Wallet.getPaymentsClient(
            activity,
            Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                .build()
        )
    }

    suspend fun isReadyToPay(): Boolean {
        return withContext(Dispatchers.IO){
            val request = IsReadyToPayRequest.fromJson(
                JSONObject()
                    .put("allowedPaymentMethods", allowedPaymentMethods())
                    .toString()
            )
            return@withContext try {
                val task = paymentsClient.isReadyToPay(request)
                com.google.android.gms.tasks.Tasks.await(task)
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    private fun formatTotalPrice(amount: BigDecimal, currencyCode: String): String {
        val scale = Currency.getInstance(currencyCode).defaultFractionDigits // UAH -> 2
        val normalized = amount.setScale(scale.coerceAtLeast(0), RoundingMode.HALF_UP)
        return normalized.toPlainString()
    }


    fun createPaymentTask(amount: Float, currency: String): com.google.android.gms.tasks.Task<PaymentData> {
        val amountStr = formatTotalPrice(BigDecimal(amount.toDouble()), currency)
        val req = PaymentDataRequest.fromJson(paymentDataRequestJson(amountStr, currency).toString())
            ?: error("Invalid PaymentDataRequest")
        return paymentsClient.loadPaymentData(req)
    }

    fun extractPaymentMethodToken(paymentData: PaymentData): String {
        val json = JSONObject(paymentData.toJson())
        return json
            .getJSONObject("paymentMethodData")
            .getJSONObject("tokenizationData")
            .getString("token")
    }

    private fun paymentDataRequestJson(amount: String, currency: String): JSONObject {
        return JSONObject()
            .put("apiVersion", 2)
            .put("apiVersionMinor", 0)
            .put("allowedPaymentMethods", allowedPaymentMethodsWithTokenization())
            .put(
                "transactionInfo",
                JSONObject()
                    .put("totalPriceStatus", "FINAL")
                    .put("totalPrice", amount)          // "9.99"
                    .put("currencyCode", currency)      // "USD" | "EUR" | "UAH" ...
                    .put("countryCode", "UA")           // ВАЖНО: код страны процессинга/мерчанта
            )
            .put(
                "merchantInfo",
                JSONObject()
                    .put("merchantName", "Example Test Merchant")

            )
    }

    private fun allowedPaymentMethods(): org.json.JSONArray =
        org.json.JSONArray().put(
            JSONObject()
                .put("type", "CARD")
                .put("parameters", JSONObject()
                    .put("allowedAuthMethods", org.json.JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS"))
                    .put("allowedCardNetworks", org.json.JSONArray().put("VISA").put("MASTERCARD"))
                )
        )

    private fun allowedPaymentMethodsWithTokenization(): org.json.JSONArray =
        org.json.JSONArray().put(
            JSONObject()
                .put("type", "CARD")
                .put("parameters", JSONObject()
                    .put("allowedAuthMethods", org.json.JSONArray().put("PAN_ONLY").put("CRYPTOGRAM_3DS"))
                    .put("allowedCardNetworks", org.json.JSONArray().put("VISA").put("MASTERCARD"))
                    .put("billingAddressRequired", false)
                )
                .put("tokenizationSpecification", JSONObject()
                    .put("type", "PAYMENT_GATEWAY")
                    .put("parameters", JSONObject()
                        .put("gateway", "example")   // тестовый gateway
                        .put("gatewayMerchantId", "exampleGatewayMerchantId")
                    )
                )
        )
}