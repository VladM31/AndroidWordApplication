package can.lucky.of.addword.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.addword.domain.models.states.AddWordByImageState
import can.lucky.of.addword.domain.actions.AddWordByImageAction
import can.lucky.of.addword.domain.actions.AddWordByTextAction
import can.lucky.of.addword.domain.managers.WordRecognizeManager
import can.lucky.of.addword.domain.models.WordImageOptions
import can.lucky.of.addword.domain.models.WordTextOptions
import can.lucky.of.addword.domain.models.states.AddWordByTextState
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.results.ConfirmBox
import can.lucky.of.core.domain.vms.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class AddWordByTextVm(
    private val wordRecognizeManager: WordRecognizeManager
) : ViewModel(), MviViewModel<AddWordByTextState, AddWordByTextAction> {

    private val mutableState = MutableStateFlow(AddWordByTextState())
    override val state: StateFlow<AddWordByTextState> = mutableState

    override fun sent(action: AddWordByTextAction) {
        when (action) {
            is AddWordByTextAction.SetLanguage -> handleSetLanguage(action)
            is AddWordByTextAction.SetTranslateLanguage -> handleSetTranslateLanguage(action)
            is AddWordByTextAction.SetOriginal -> handleSetOriginal(action)
            is AddWordByTextAction.SetTranslate -> handleSetTranslate(action)
            is AddWordByTextAction.Recognize -> handleRecognize()
        }
    }

    private fun handleSetLanguage(language: AddWordByTextAction.SetLanguage) {
        mutableState.value = mutableState.value.copy(language = language.language)
    }

    private fun handleSetTranslateLanguage(language: AddWordByTextAction.SetTranslateLanguage) {
        mutableState.value = mutableState.value.copy(translateLang = language.language)
    }

    private fun handleSetOriginal(original: AddWordByTextAction.SetOriginal) {
        mutableState.value = mutableState.value.copy(original = original.original)
    }

    private fun handleSetTranslate(translate: AddWordByTextAction.SetTranslate) {
        mutableState.value = mutableState.value.copy(translate = translate.translate)
    }

    private fun handleRecognize() {
        val message = when {
            state.value.let { it.translate + it.original }.isBlank() -> "Please enter a word"
            state.value.original.trim().let { it.isNotBlank() && it.length < 2 } -> "Please enter a valid word"
            state.value.translate.trim().let { it.isNotBlank() && it.length < 2 } -> "Please enter a valid translation"
            state.value.language == state.value.translateLang -> "Please select different languages"
            else -> ""
        }

        if (message.isNotEmpty()) {
            mutableState.value = mutableState.value.copy(errorMessage = ErrorMessage(message = message))
            return
        }


        viewModelScope.launch(Dispatchers.IO) {
            val wordOption = WordTextOptions(
                original = state.value.original,
                translate = state.value.translate,
                wordLang = state.value.language,
                translateLang = state.value.translateLang
            )

            try {
                mutableState.value = state.value.copy(isLoading = true)
                val word = wordRecognizeManager.recognize(wordOption)
                mutableState.value = mutableState.value.copy(word = word)
            }catch (e: Exception){
                mutableState.value = state.value.copy(
                    errorMessage = ErrorMessage(message = e.message.orEmpty()),
                    isLoading = false
                )
            }



        }


    }
}