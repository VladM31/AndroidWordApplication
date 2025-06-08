package can.lucky.of.addword.domain.vms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.addword.domain.models.states.AddWordByImageState
import can.lucky.of.addword.domain.actions.AddWordByImageAction
import can.lucky.of.addword.domain.managers.WordRecognizeManager
import can.lucky.of.addword.domain.models.WordImageOptions
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.results.ConfirmBox
import can.lucky.of.core.domain.vms.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class AddWordByImageVm(
    private val wordRecognizeManager: WordRecognizeManager
) : ViewModel(), MviViewModel<AddWordByImageState, AddWordByImageAction> {

    private val mutableState = MutableStateFlow(AddWordByImageState())
    override val state: StateFlow<AddWordByImageState> = mutableState

    override fun sent(action: AddWordByImageAction) {
        when (action) {
            is AddWordByImageAction.SetLanguage -> handleSetLanguage(action)
            is AddWordByImageAction.SetTranslateLanguage -> handleSetTranslateLanguage(action)
            is AddWordByImageAction.SetImage -> handleSetImage(action)
            is AddWordByImageAction.Confirm -> handleConfirm()
        }
    }

    private fun handleSetLanguage(language: AddWordByImageAction.SetLanguage) {
        mutableState.value = mutableState.value.copy(language = language.language)
    }

    private fun handleSetTranslateLanguage(language: AddWordByImageAction.SetTranslateLanguage) {
        mutableState.value = mutableState.value.copy(translateLanguage = language.language)
    }

    private fun handleSetImage(image: AddWordByImageAction.SetImage) {
        mutableState.value = mutableState.value.copy(image = image.image)
    }

    private fun handleConfirm() {
        val message = when {
            state.value.language == Language.UNDEFINED -> "Please select a language"
            state.value.translateLanguage == Language.UNDEFINED -> "Please select a language to translate"
            state.value.language == state.value.translateLanguage -> "Please select different languages"
            state.value.image == null -> "Please select an image"
            else -> ""
        }

        if (message.isNotEmpty()) {
            mutableState.value = mutableState.value.copy(error = ErrorMessage(message = message))
            return
        }


        viewModelScope.launch(Dispatchers.IO) {
            val wordOption = WordImageOptions(
                imageUri = state.value.image!!,
                wordLang = state.value.language,
                translateLang = state.value.translateLanguage
            )

            try {
                mutableState.value = state.value.copy(isLoading = true)
                val word = wordRecognizeManager.recognize(wordOption)
                mutableState.value = mutableState.value.copy(word = word)
            }catch (e: Exception){
                mutableState.value = state.value.copy(
                    error = ErrorMessage(message = e.message.orEmpty()),
                    isLoading = false
                )
            }



        }


    }
}