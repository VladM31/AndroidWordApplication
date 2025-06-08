package can.lucky.of.exercise.domain.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import can.lucky.of.core.domain.managers.playlist.PlayListManager
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.managers.userwords.UserWordManager
import can.lucky.of.core.domain.models.data.playlists.PlayListGrade
import can.lucky.of.core.domain.models.data.words.GradeUserWord
import can.lucky.of.core.domain.vms.MviViewModel
import can.lucky.of.exercise.domain.actions.ExerciseAction
import can.lucky.of.exercise.domain.managers.ExerciseStatisticalManager
import can.lucky.of.exercise.domain.managers.ExerciseTransactionManager
import can.lucky.of.exercise.domain.managers.ExerciseWordManager
import can.lucky.of.exercise.domain.mappers.toStartExerciseTransaction
import can.lucky.of.exercise.domain.models.data.EndExerciseTransaction
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import can.lucky.of.exercise.domain.models.filters.ExerciseTransactionFilter
import can.lucky.of.exercise.domain.models.filters.ExerciseWordFilter
import can.lucky.of.exercise.domain.models.states.ExerciseState

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class ExerciseViewModel(
    private val exerciseWordManager: ExerciseWordManager,
    private val exerciseTransactionManager: ExerciseTransactionManager,
    private val subscribeCacheManager: SubscribeCacheManager,
    private val playListManager: PlayListManager,
    private val userWordManager: UserWordManager,
    private val exerciseStatisticalManager: ExerciseStatisticalManager
) : ViewModel(), MviViewModel<ExerciseState, ExerciseAction> {
    private val mutableState = MutableStateFlow(ExerciseState())
    override val state: StateFlow<ExerciseState> = mutableState

    override fun sent(action: ExerciseAction) {
        when (action) {
            is ExerciseAction.Init -> handleInit(action)
            is ExerciseAction.NextExercise -> handleNextExercise(action)
        }
    }

    private fun handleNextExercise(action: ExerciseAction.NextExercise) {
        val newWords = mutableListOf<ExerciseWordDetails>()
        state.value.words.forEachIndexed { index, word ->
            newWords.add(word.copy(grade = action.words[index].grade))
        }
        val newExercises = state.value.exercises.drop(1)

        val isEnd = newExercises.isEmpty()

        if (isEnd.not()) {
            mutableState.value = state.value.copy(
                exercises = newExercises,
                words = newWords,
                isEnd = false
            )
            return
        }


        if (state.value.playListId == null) {
            saveByWordIds(newWords)
            return
        }

        saveByPlayListId(newWords)
    }

    private fun saveByWordIds(newWords: MutableList<ExerciseWordDetails>){
        viewModelScope.launch(Dispatchers.IO) {
            userWordManager.runCatching {
                putGrades(newWords.map { it.toGradeWord() })
            }
        }
    }

    private fun saveByPlayListId(newWords: MutableList<ExerciseWordDetails>) {
        viewModelScope.launch(Dispatchers.IO) {
            playListManager.runCatching {
                updateGrades(newWords.map { it.toPlayListGradeWord() })
            }
            exerciseStatisticalManager.endExercise(EndExerciseTransaction(transactionId = state.value.transactionId))
            mutableState.value = state.value.copy(isEnd = true)
        }
    }

    private fun handleInit(action: ExerciseAction.Init) {
        if (state.value.isInited) return
        mutableState.value = state.value.copy(
            isInited = true,
            transactionId = action.transactionId,
            playListId = action.playListId
        )

        viewModelScope.launch(Dispatchers.IO) {
            val transaction = exerciseTransactionManager.findBy(ExerciseTransactionFilter(action.transactionId))
            val words = exerciseWordManager.findDetailsBy(ExerciseWordFilter(action.transactionId))
            val isActiveSubscribe = subscribeCacheManager.isActiveSubscribe()

            val isEnd = transaction?.isExecuted ?: false || words.isEmpty() || transaction?.exercises?.isEmpty() ?: true

            transaction?.let {
                mutableState.value = state.value.copy(
                    exerciseTransaction = it,
                    exercises = it.exercises,
                    words = words,
                    isEnd = isEnd,
                    isActiveSubscribe = isActiveSubscribe
                )
                exerciseStatisticalManager.startExercise(mutableState.value.toStartExerciseTransaction())
            }


        }
    }

    private fun ExerciseWordDetails.toPlayListGradeWord() : PlayListGrade {
        return PlayListGrade(
            wordId = this.userWordId,
            playListGrade = this.grade.toLong(),
            wordGrade = this.grade.toLong()
        )
    }

    private fun ExerciseWordDetails.toGradeWord() : GradeUserWord{
        return GradeUserWord(
            userWordId = this.userWordId,
            value = this.grade.toLong()
        )
    }
}