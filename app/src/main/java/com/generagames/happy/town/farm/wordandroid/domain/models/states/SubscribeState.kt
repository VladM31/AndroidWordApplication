package com.generagames.happy.town.farm.wordandroid.domain.models.states

import can.lucky.of.core.utils.titleCase

data class SubscribeState(
    val condition: SubscribeCondition = SubscribeCondition.INACTIVE,
    val expirationDate: String = "",
){

    enum class SubscribeCondition {
        ACTIVE, INACTIVE;

        private val titleNameCase = name.titleCase()

        fun toTitleCase(): String {
            return titleNameCase
        }
    }
}
