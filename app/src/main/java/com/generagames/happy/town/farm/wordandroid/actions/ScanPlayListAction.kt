package com.generagames.happy.town.farm.wordandroid.actions

interface ScanPlayListAction {
    data class SetId(val id: String) : ScanPlayListAction
    data class SetError(val error: String) : ScanPlayListAction
    data object Accept : ScanPlayListAction
}