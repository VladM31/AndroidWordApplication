package com.generagames.happy.town.farm.wordandroid.domain.managers.payment

import android.content.Context
import can.lucky.of.addword.utils.readValueOrNull
import can.lucky.of.addword.utils.writeValueAsStringOrNull
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SubCost

internal class PayPropositionManagerImpl(
    context: Context
) : PayPropositionManager {
    private val sharedPreferences =
        context.getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE)

    override fun getProposition(): SubCost? {
        val json = sharedPreferences.getString(PROPOSITION_KEY, null)
        return json?.let {
            readValueOrNull(it, SubCost::class.java)
        }
    }

    override fun setProposition(subCost: SubCost) {
        writeValueAsStringOrNull(subCost)?.let { json ->
            sharedPreferences.edit()
                .putString(PROPOSITION_KEY, json)
                .apply()
        }
    }

    companion object {
        private const val PROPOSITION_KEY = "PROPOSITION_KEY"
        private const val SHARED_NAME = "PayPropositionManagerImpl"
    }
}
