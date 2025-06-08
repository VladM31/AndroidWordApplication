package com.generagames.happy.town.farm.wordandroid.actions

import can.lucky.of.core.domain.models.filters.PlayListCountFilter


sealed interface PlayListAction{
    data object ReFetch : PlayListAction
    data class UpdateFilter(val filter: PlayListCountFilter) : PlayListAction
}