package com.generagames.happy.town.farm.wordandroid.actions

import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.PlayListFilterBundle

sealed interface PlayListFilterAction {
    data class Init(val bundle: PlayListFilterBundle) : PlayListFilterAction
    data class ChangeStartCount(val startCount: String) : PlayListFilterAction
    data class ChangeEndCount(val endCount: String) : PlayListFilterAction
    data class ChangeName(val name: String) : PlayListFilterAction
    data object Find : PlayListFilterAction
}