package can.lucky.of.exercise.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.ui.fragments.LoadingFragment
import can.lucky.of.core.utils.onEnd
import can.lucky.of.exercise.domain.vm.ExerciseViewModel
import can.lucky.of.exercise.R
import can.lucky.of.exercise.domain.actions.ExerciseAction
import can.lucky.of.exercise.domain.models.states.ExerciseState
import can.lucky.of.exercise.ui.fragments.LetterMatchByDescriptionFragment
import can.lucky.of.exercise.ui.fragments.MatchExerciseFragment
import can.lucky.of.exercise.ui.fragments.LetterMatchByFieldFragment
import can.lucky.of.exercise.ui.fragments.LetterMatchByTranslationFragment
import can.lucky.of.exercise.ui.fragments.OptionWordByDescriptionFragment
import can.lucky.of.exercise.ui.fragments.OptionWordByOriginalFragment
import can.lucky.of.exercise.ui.fragments.OptionWordByTranslateFragment
import can.lucky.of.exercise.ui.fragments.OptionDescriptionByWordsFragment
import can.lucky.of.exercise.ui.fragments.WriteByImageAndDescriptionFragment
import can.lucky.of.exercise.ui.fragments.WriteByImageAndTranslationFragment

import can.lucky.of.exercise.ui.handels.ExerciseHandle
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExerciseActivity : AppCompatActivity(), ExerciseHandle {
    private val vm by viewModel<ExerciseViewModel>()

    private val fragmentMap = mapOf(
        Exercise.COMPARE.name to MatchExerciseFragment::class.java,

        Exercise.WORD_BY_TRANSLATES.name to OptionWordByTranslateFragment::class.java,
        Exercise.WORD_BY_ORIGINALS.name to OptionWordByOriginalFragment::class.java,

        Exercise.WORD_BY_DESCRIPTIONS.name to OptionWordByDescriptionFragment::class.java,
        Exercise.DESCRIPTION_BY_WORDS.name to OptionDescriptionByWordsFragment::class.java,

        Exercise.LETTERS_MATCH_BY_TRANSLATION.name to LetterMatchByTranslationFragment::class.java,
        Exercise.LETTERS_MATCH_BY_DESCRIPTION.name to LetterMatchByDescriptionFragment::class.java,

        Exercise.WORD_BY_WRITE_TRANSLATE.name to WriteByImageAndTranslationFragment::class.java,
        Exercise.WORD_BY_WRITE_BY_DESCRIPTION.name to WriteByImageAndDescriptionFragment::class.java,


    )

    override val state: ExerciseState
        get() = vm.state.value


    override fun sent(action: ExerciseAction) {
        vm.sent(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)
        supportActionBar?.hide()

        val bundle = intent.extras?.let {
            ExerciseActivityArgs.fromBundle(it)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.exerciseFragmentContainer, LoadingFragment()).commit()
        }

        if (vm.state.value.isInited.not()) {
            bundle?.let {
                vm.sent(ExerciseAction.Init(bundle.transactionId, bundle.playListId))
            }
        }

        lifecycleScope.launch {
            vm.state.buffer(0).map { it.exercises.firstOrNull() }
                .distinctUntilChanged().collectLatest {

                    fragmentMap[it?.name]?.let { fragment ->
                        if (containsFragment(fragment)) {
                            return@collectLatest
                        }
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.exerciseFragmentContainer, fragment, null)
                            .commit()
                    }
                }
        }

        vm.state.onEnd(lifecycleScope){
            finish()
        }
    }

    private fun containsFragment(clazz: Class<out Fragment>): Boolean {
        return supportFragmentManager.fragments.any { it.javaClass == clazz }
    }


}