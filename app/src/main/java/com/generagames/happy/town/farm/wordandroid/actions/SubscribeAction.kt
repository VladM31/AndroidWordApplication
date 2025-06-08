package com.generagames.happy.town.farm.wordandroid.actions

sealed interface SubscribeAction{
    data object UpdateState : SubscribeAction
}