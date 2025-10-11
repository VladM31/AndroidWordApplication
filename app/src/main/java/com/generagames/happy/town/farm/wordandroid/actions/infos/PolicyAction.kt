package com.generagames.happy.town.farm.wordandroid.actions.infos

interface PolicyAction {

    data object End : PolicyAction

    data class Error(val message: String) : PolicyAction
}