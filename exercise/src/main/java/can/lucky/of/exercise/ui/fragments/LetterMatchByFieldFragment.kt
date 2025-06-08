package can.lucky.of.exercise.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.allViews
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.dp
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.toList
import can.lucky.of.exercise.R
import can.lucky.of.exercise.databinding.FragmentLetterMatchBinding
import can.lucky.of.exercise.domain.actions.ExerciseAction
import can.lucky.of.exercise.domain.actions.LettersMatchAction
import can.lucky.of.exercise.domain.models.data.ExerciseWord
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import can.lucky.of.exercise.domain.models.states.LettersMatchState
import can.lucky.of.exercise.domain.vm.LettersMatchVm
import can.lucky.of.exercise.ui.handels.ExerciseHandle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


open class LetterMatchByFieldFragment(
    private val exerciseType: Exercise,
    private val toText: (ExerciseWordDetails) -> String,
) : Fragment(R.layout.fragment_letter_match) {
    private var binding: FragmentLetterMatchBinding? = null
    private val vm: LettersMatchVm by viewModel()
    private val primaryGreen by lazy {
        requireActivity().getColor(can.lucky.of.core.R.color.primary_green)
    }
    private val primaryRed by lazy {
        requireActivity().getColor(can.lucky.of.core.R.color.primary_red)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentLetterMatchBinding.bind(view)
        binding = newBinding


        lifecycleScope.launch {
            vm.state.filter { it.words.isNotEmpty() }
                .map { it.currentWord() }
                .distinctUntilChanged()
                .collectLatest {
                    binding?.translateText?.text = toText(it)
                }
        }


        if (vm.state.value.isInited.not()) {
            (requireActivity() as? ExerciseHandle)?.state?.let { state ->
                vm.sent(LettersMatchAction.Init(state.words, state.isActiveSubscribe,state.transactionId, exerciseType))
            }
        }

        lifecycleScope.launch {
            vm.state.map { it.letters }
                .distinctUntilChanged()
                .collectLatest(this@LetterMatchByFieldFragment::handleLetters)
        }
        lifecycleScope.launch {
            vm.state.map { it.resultWord }
                .distinctUntilChanged()
                .collectLatest {
                    binding?.wordText?.text = it
                }
        }

        lifecycleScope.launch {
            vm.state.map { it.errorLetter }
                .distinctUntilChanged()
                .collectLatest(this@LetterMatchByFieldFragment::handleErrorLetter)
        }

        lifecycleScope.launch {
            vm.state.map { it.isNext }
                .distinctUntilChanged()
                .collectLatest {
                    binding?.nextButton?.visibility = if (it) View.VISIBLE else View.GONE
                }
        }

        binding?.nextButton?.setOnClickListener {
            vm.sent(LettersMatchAction.Next)
        }

        handleEnd()
        handleSetToolBar()
    }

    private fun handleSetToolBar() {
        ToolBarController(
            binding = binding?.toolBar ?: return,
            navController = null,
            title = Exercise.LETTERS_MATCH_BY_TRANSLATION.text,
            buttonImage = can.lucky.of.core.R.drawable.plus_one,
            buttonAction = {
                vm.sent(LettersMatchAction.PlusOneLetter)
            }
        ).apply {
            setDefaultSettings()
            setNavigateUp {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun handleEnd() {
        val handle = requireActivity() as? ExerciseHandle

        vm.state.onEnd(lifecycleScope) {
            vm.state.value.words.mapIndexed { index, word ->
                ExerciseWord(
                    userWordId = word.userWordId,
                    grade = word.grade + vm.state.value.grades.getOrElse(index) { 0 },
                    transactionId = word.transactionId,
                )
            }.let {
                handle?.sent(ExerciseAction.NextExercise(words = it))
            }
        }
    }

    private fun LettersMatchState.Letter.toTextView(): View {
        val letterObj = this
        return TextView(requireContext()).apply {
            text = letterObj.letter.toString()
            tag = letter

            setTextColor(primaryGreen)

            val sizePx = resources.getDimension(R.dimen.letter_match_field_size)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, sizePx)
            layoutParams = MarginLayoutParams(
                MarginLayoutParams.WRAP_CONTENT,
                MarginLayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(3)
            }
        }
    }

    private fun LettersMatchState.Letter.toLayout(): ViewGroup {
        val paddingHorizontal = 12.dp.toInt()
        val paddingVertical = 3.dp.toInt()

        return LinearLayout(requireContext()).apply {
            tag = this@toLayout
            setBackgroundResource(can.lucky.of.core.R.drawable.box_back)
            setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
            layoutParams = MarginLayoutParams(
                MarginLayoutParams.WRAP_CONTENT,
                MarginLayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(5.dp.toInt())
            }
            addView(toTextView())
            setOnClickListener {
                vm.sent(
                    LettersMatchAction.ClickOnLetter(
                        this@toLayout.letter,
                        this@toLayout.id
                    )
                )
            }
        }
    }

    private fun handleLetters(letters: List<LettersMatchState.Letter>) {
        val views = binding?.flexBox?.toList() ?: return

        if (views.size < letters.size) {
            binding?.flexBox?.removeAllViews()
            letters.forEach { letter ->
                binding?.flexBox?.addView(letter.toLayout())
            }
            return
        }

        views.forEach {
            if (letters.contains(it.tag)) {
                return@forEach
            }
            binding?.flexBox?.removeView(it)
        }
    }

    private fun handleErrorLetter(errorLetter: LettersMatchState.ErrorLetter?) {
        binding?.flexBox?.isClickable = errorLetter == null
        val views = binding?.flexBox?.toList() ?: return
        views.forEach {
            if (it.tag == errorLetter?.letter) {
                it.setBackgroundResource(can.lucky.of.core.R.drawable.box_red_back)
                it.allViews.forEach { view ->
                    if (view is TextView) {
                        view.setTextColor(primaryRed)
                    }
                }
            } else {
                it.setBackgroundResource(can.lucky.of.core.R.drawable.box_back)
                it.allViews.forEach { view ->
                    if (view is TextView) {
                        view.setTextColor(primaryGreen)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}