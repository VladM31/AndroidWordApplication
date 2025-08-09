package can.lucky.of.addword.domain.models.states

import androidx.paging.PagingData
import can.lucky.of.addword.domain.models.AiRecognizeResult
import can.lucky.of.addword.domain.models.filters.RecognizeResultFilter
import can.lucky.of.core.domain.models.data.ErrorMessage
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.states.ErrorableState

data class RecognizeWordTasksState(
    val filter: RecognizeResultFilter = RecognizeResultFilter(page = 0, size = 10),
    val pagingData: PagingData<AiRecognizeResult> = PagingData.empty(),
    val word: Word? = null,
    val isLoading: Boolean = true,
    override val errorMessage: ErrorMessage? = null,
): ErrorableState
