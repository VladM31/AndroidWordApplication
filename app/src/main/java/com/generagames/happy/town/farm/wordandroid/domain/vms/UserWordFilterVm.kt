package com.generagames.happy.town.farm.wordandroid.domain.vms

import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.UserWordSortBy
import can.lucky.of.core.domain.vms.AbstractMviViewModel
import com.generagames.happy.town.farm.wordandroid.actions.UserWordFilterAction
import com.generagames.happy.town.farm.wordandroid.domain.models.states.UserWordFilterState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class UserWordFilterVm : AbstractMviViewModel<UserWordFilterState, UserWordFilterAction>(){
    private val mutableState = MutableStateFlow(UserWordFilterState())
    override val state: StateFlow<UserWordFilterState> = mutableState

    override fun sent(action: UserWordFilterAction) {
        mutableState.value = when(action){
            is UserWordFilterAction.Init -> {
                handleInit(action)
            }
            is UserWordFilterAction.SetOriginal -> {
                state.value.copy(original = action.original.ifBlank { null })
            }
            is UserWordFilterAction.SetTranslate -> {
                state.value.copy(translate = action.translate.ifBlank { null })
            }
            is UserWordFilterAction.SetLang -> {
                state.value.copy(lang = if (action.lang == Language.UNDEFINED) null else action.lang)
            }
            is UserWordFilterAction.SetTranslateLang -> {
                state.value.copy(translateLang =  if (action.lang == Language.UNDEFINED) null else action.lang)
            }
            is UserWordFilterAction.SetCategories -> {
                handleCategories(action)
            }
            is UserWordFilterAction.SetSortBy -> {
                state.value.copy(sortBy = UserWordSortBy.fromTitleCase(action.sortBy) ?: UserWordSortBy.DATE_OF_ADDED)
            }
            is UserWordFilterAction.SetAsc -> {
                state.value.copy(asc = action.asc)
            }
            is UserWordFilterAction.SetCefr -> {
                state.value.copy(cefr = action.cefr)
            }
        }
    }

    private fun handleCategories(action: UserWordFilterAction.SetCategories) : UserWordFilterState {
        if (action.categories.isEmpty()) {
            return state.value.copy(categories = null)
        }
        return state.value.copy(categories = action.categories)
    }

    private fun handleLang(lang: String) : Language? {
        val language = Language.fromTitleCase(lang)
        if (language == Language.UNDEFINED) {
            return null
        }
        return language
    }

    private fun handleInit(action: UserWordFilterAction.Init) =
        UserWordFilterState(
            original = action.bundle.original,
            translate = action.bundle.translate,
            lang = action.bundle.originalLang?.let { handleLang(it) },
            translateLang = action.bundle.translateLang?.let { handleLang(it) },
            categories = action.bundle.categories,
            sortBy = action.bundle.sortBy,
            asc = action.bundle.asc,
            isInit = true
        )
}