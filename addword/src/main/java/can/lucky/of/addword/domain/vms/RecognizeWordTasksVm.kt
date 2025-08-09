package can.lucky.of.addword.domain.vms

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import can.lucky.of.addword.domain.actions.RecognizeWordTasksAction
import can.lucky.of.addword.domain.managers.recognizes.AiRecognizeWordManager
import can.lucky.of.addword.domain.models.AiRecognizeResult
import can.lucky.of.addword.domain.models.AiRecognizedWord
import can.lucky.of.addword.domain.models.filters.RecognizeResultFilter
import can.lucky.of.addword.domain.models.states.RecognizeWordTasksState
import can.lucky.of.addword.ui.sources.AiRecognizeResultPageLoader
import can.lucky.of.addword.ui.sources.AiRecognizeResultPagingSource
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.vms.MviViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID


internal class RecognizeWordTasksVm(
    private val aiRecognizeWordManager: AiRecognizeWordManager
) : ViewModel(), MviViewModel<RecognizeWordTasksState, RecognizeWordTasksAction> {


    private val mutableState = MutableStateFlow(RecognizeWordTasksState())
    override val state: StateFlow<RecognizeWordTasksState> = mutableState

    init {
        viewModelScope.launch {
            state.map { it.filter }
                .distinctUntilChanged()
                .map { filter ->
                    getPageAiRecognizeResults(filter)
                        .cachedIn(viewModelScope)
                }
                .collectLatest { pagingDataFlow ->
                    pagingDataFlow.collect { data ->
                        mutableState.update { it.copy(pagingData = data) }
                    }
                }
        }

    }


    override fun sent(action: RecognizeWordTasksAction) {
        when (action) {
            is RecognizeWordTasksAction.Reload -> handleReload()
            is RecognizeWordTasksAction.OpenRecognizeResult -> handleOpen(action)
        }
    }

    private fun handleReload() {
        mutableState.update {
            it.copy(
                filter = it.filter.copy(
                    filterId = System.currentTimeMillis()
                )
            )
        }
    }

    private fun handleOpen(action: RecognizeWordTasksAction.OpenRecognizeResult) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recognizedWord = aiRecognizeWordManager.getWord(action.recognizeId)
                mutableState.update {
                    it.copy(word = recognizedWord.toWord())
                }
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(errorMessage = ErrorMessage(e.message.orEmpty()))
                }
            }
        }
    }


    private fun AiRecognizedWord.toWord(): Word {
        return Word(
            id = UUID.randomUUID().toString(),
            original = this.word,
            translate = this.translation,
            lang = this.language.name,
            translateLang = this.translationLanguage.name,
            cefr = this.cefr.name,
            description = this.description,
            category = this.category,
        )
    }

    private fun getPageAiRecognizeResults(filter: RecognizeResultFilter): Flow<PagingData<AiRecognizeResult>> {
        val loafer: AiRecognizeResultPageLoader = { page, pageSize ->
            val content = aiRecognizeWordManager.getRecognizeResults(
                filter.copy(
                    page = page,
                    size = pageSize
                )
            ).content
            mutableState.update { it.copy(isLoading = false) }

            content
        }



        val resultSize = 10

        return Pager(
            config = PagingConfig(
                pageSize = resultSize,
                initialLoadSize = resultSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { AiRecognizeResultPagingSource(loafer) }
        ).flow
    }
}