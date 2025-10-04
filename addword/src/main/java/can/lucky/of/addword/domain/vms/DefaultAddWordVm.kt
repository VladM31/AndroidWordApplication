package can.lucky.of.addword.domain.vms

import android.util.Log
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.addword.domain.actions.DefaultAddWordAction
import can.lucky.of.addword.domain.models.states.DefaultAddWordState
import can.lucky.of.addword.domain.valid.defaultAddWordValidator
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.managers.userwords.UserWordManager
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.data.words.SaveWord
import can.lucky.of.core.domain.vms.MviViewModel
import can.lucky.of.validation.schemes.length
import can.lucky.of.validation.schemes.notBlank
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


internal class DefaultAddWordVm(
    private val wordManager: UserWordManager,
    private val subscribeCacheManager: SubscribeCacheManager
) : ViewModel(), MviViewModel<DefaultAddWordState, DefaultAddWordAction> {

    private val mutableState = MutableStateFlow(DefaultAddWordState())
    override val state: StateFlow<DefaultAddWordState> = mutableState
    private val validator = defaultAddWordValidator(mutableState)



    private fun checkSubscribe(action: DefaultAddWordAction.Init) {
        viewModelScope.launch(Dispatchers.IO) {
            mutableState.value =
                mutableState.value.copy(isSubscribe = subscribeCacheManager.isActiveSubscribe())

            val schema = validator.getSchema<String>("Translation")

            state.value.isSubscribe?.let {
                if (it) {
                    schema?.notBlank(canBeEmpty = true)
                } else {
                    schema?.notBlank()?.length(min = 2, max = 255)
                }
            }
            handleInit(action)
        }
    }

    override fun sent(action: DefaultAddWordAction) {

        if (state.value.isInited.not()){
            if (action is DefaultAddWordAction.Init){
                checkSubscribe(action)
            }
            return
        }

        when (action) {
            is DefaultAddWordAction.Add -> handleAdd()
            is DefaultAddWordAction.ChangeLanguage -> setLanguage(action)
            is DefaultAddWordAction.ChangeTranslationLanguage -> setTranslationLanguage(action)
            is DefaultAddWordAction.SetWord -> setWord(action)
            is DefaultAddWordAction.SetTranslation -> setTranslation(action)
            is DefaultAddWordAction.SetDescription -> setDescription(action)
            is DefaultAddWordAction.SetCategory -> setCategory(action)
            is DefaultAddWordAction.SetCefr -> setCefr(action)
            is DefaultAddWordAction.SetNeedSound -> setNeedSound(action)
            is DefaultAddWordAction.SetSound -> setSound(action)
            is DefaultAddWordAction.SetImage -> setImage(action)
            else -> {}
        }
    }

    private fun DefaultAddWordState.toInsertWord() = SaveWord(
        word = word,
        language = language,
        translationLanguage = translationLanguage,
        translation = translation,
        category = category.ifBlank { null },
        description = description.ifBlank { null },
        cefr = cefr,
        image = image,
        sound = sound,
        needSound = needSound
    )

    private fun handleAdd() {
        val message = validator.validate(" - ")

        if (message.isNotBlank()) {
            mutableState.value =
                mutableState.value.copy(errorMessage = ErrorMessage(message = message))
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            val insertWord = mutableState.value.toInsertWord()
            mutableState.value = mutableState.value.copy(isLoading = true)
            try {
                wordManager.save(listOf(insertWord))
                mutableState.value = mutableState.value.copy(isEnd = true)
            }catch (e: Exception){
                Log.e("DefaultAddWordVm", "handleAdd: ", e)
                mutableState.value = mutableState.value.copy(
                    errorMessage = ErrorMessage(message = e.message ?: "Unknown error"),
                    isLoading = false
                )
            }
        }
    }

    private fun setLanguage(action: DefaultAddWordAction.ChangeLanguage) {
        mutableState.value = mutableState.value.copy(language = action.language)
    }

    private fun setTranslationLanguage(action: DefaultAddWordAction.ChangeTranslationLanguage) {
        mutableState.value = mutableState.value.copy(translationLanguage = action.language)
    }

    private fun setWord(action: DefaultAddWordAction.SetWord) {
        mutableState.value = mutableState.value.copy(word = action.word)
    }

    private fun setTranslation(action: DefaultAddWordAction.SetTranslation) {
        mutableState.value = mutableState.value.copy(translation = action.translation)
    }

    private fun setDescription(action: DefaultAddWordAction.SetDescription) {
        mutableState.value = mutableState.value.copy(description = action.description)
    }

    private fun setCategory(action: DefaultAddWordAction.SetCategory) {
        mutableState.value = mutableState.value.copy(category = action.category)
    }

    private fun setCefr(action: DefaultAddWordAction.SetCefr) {
        mutableState.value = mutableState.value.copy(cefr = action.cefr)
    }

    private fun setNeedSound(action: DefaultAddWordAction.SetNeedSound) {
        mutableState.value = mutableState.value.copy(needSound = action.needSound)
    }

    private fun setSound(action: DefaultAddWordAction.SetSound) {
        mutableState.value = mutableState.value.copy(sound = action.sound)
    }

    private fun setImage(action: DefaultAddWordAction.SetImage) {
        mutableState.value = mutableState.value.copy(image = action.image)
    }

    private fun handleInit(action: DefaultAddWordAction.Init){
        if (action.word == null) {
            mutableState.value = mutableState.value.copy(isInited = true)
            return
        }
        mutableState.value = state.value.copy(
            word = action.word.original,
            language = action.word.lang,
            translationLanguage = action.word.translateLang,
            translation = action.word.translate,
            category = action.word.category ?: "",
            description = action.word.description ?: "",
            cefr = action.word.cefr,
            image = action.word.imageLink?.toUri()?.toFile(),
            isInited = true
        )
    }
}