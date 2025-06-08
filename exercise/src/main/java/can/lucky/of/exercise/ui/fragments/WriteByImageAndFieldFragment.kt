package can.lucky.of.exercise.ui.fragments


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import can.lucky.of.core.domain.factories.GlideHeaderFactory
import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.addDebounceAfterTextChangedListener
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.setImage
import can.lucky.of.exercise.R
import can.lucky.of.exercise.domain.actions.ExerciseAction
import can.lucky.of.exercise.domain.actions.WriteByImageAndTranslateExerciseAction
import can.lucky.of.exercise.databinding.FragmentWriteByImageAndTranslateExerciseBinding
import can.lucky.of.exercise.domain.models.data.ExerciseWord
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import can.lucky.of.exercise.domain.vm.WriteByImageAndFieldVm
import can.lucky.of.exercise.ui.activities.ExerciseActivity
import can.lucky.of.exercise.ui.handels.ExerciseHandle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR

open class WriteByImageAndFieldFragment(
    private val exerciseType: Exercise = Exercise.WORD_BY_WRITE_TRANSLATE,
    private val toText: (ExerciseWordDetails) -> String,
) :
    Fragment(R.layout.fragment_write_by_image_and_translate_exercise) {
    private var binding: FragmentWriteByImageAndTranslateExerciseBinding? = null
    private val vm by viewModel<WriteByImageAndFieldVm>()
    private val headerFactory = get<GlideHeaderFactory>()
    private val primaryColor by lazy {
        requireContext().resources.getColor(CoreR.color.primary_green, requireContext().theme)
    }
    private val primaryRed by lazy {
        requireContext().resources.getColor(CoreR.color.primary_red, requireContext().theme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentWriteByImageAndTranslateExerciseBinding.bind(view)
        binding = newBinding


        lifecycleScope.launch {
            vm.state.map { it.isEditEnable }.distinctUntilChanged().collectLatest {
                newBinding.originalWordInput.isEnabled = it
            }
        }

        handleInit()

        newBinding.addLetter.setOnClickListener {
            vm.sent(WriteByImageAndTranslateExerciseAction.AddLetter)
        }

        newBinding.originalWordInput.addDebounceAfterTextChangedListener(300) {
            vm.sent(WriteByImageAndTranslateExerciseAction.UpdateText(it))
        }

        handleConfirmButton(newBinding)

        handleConfirmBack(newBinding)

        lifecycleScope.launch {
            vm.state.map { it.wordText }.distinctUntilChanged().collectLatest {
                if (newBinding.originalWordInput.text.toString() != it) {
                    newBinding.originalWordInput.setText(it.orEmpty())
                    newBinding.originalWordInput.setSelection(it?.length ?: 0)
                }
            }
        }

        lifecycleScope.launch {
            vm.state.map { it.wordIndex }.distinctUntilChanged().collectLatest { index ->
                newBinding.translateWord.text = toText(vm.state.value.words[index])
            }
        }

        handleSetImage(newBinding)


        handleSetToolBar(newBinding)

        handleEnd()
    }

    private fun handleEnd() {
        val handle = requireActivity() as? ExerciseHandle

        vm.state.onEnd(lifecycleScope){
            vm.state.value.words.mapIndexed { index, exerciseWordDetails ->
                ExerciseWord(
                    userWordId = exerciseWordDetails.userWordId,
                    grade = exerciseWordDetails.grade + vm.state.value.grades.getOrElse(index) { 0 },
                    transactionId = exerciseWordDetails.transactionId
                )
            }.let {
                handle?.sent(ExerciseAction.NextExercise(words = it))
            }
        }
    }

    private fun handleInit() {
        if (vm.state.value.isInited.not()) {
            (requireActivity() as? ExerciseActivity)?.state?.let {
                vm.sent(
                    WriteByImageAndTranslateExerciseAction
                        .Init(
                            it.words,
                            transactionId = it.transactionId,
                            exerciseType = this.exerciseType,
                            isActiveSubscribe = it.isActiveSubscribe
                        )
                )
            }
        }
    }

    private fun handleConfirmButton(newBinding: FragmentWriteByImageAndTranslateExerciseBinding) {
        lifecycleScope.launch {
            vm.state.map { it.isConfirm }.distinctUntilChanged().collectLatest { isConfirm ->
                if (isConfirm == true) {
                    newBinding.nextButton.text = "Next"
                    newBinding.nextButton.setOnClickListener {
                        vm.sent(WriteByImageAndTranslateExerciseAction.NextWord)
                    }
                    return@collectLatest
                }
                newBinding.nextButton.text = "Confirm"
                newBinding.nextButton.setOnClickListener {
                    vm.sent(WriteByImageAndTranslateExerciseAction.Confirm)
                }
            }
        }
    }

    private fun handleConfirmBack(newBinding: FragmentWriteByImageAndTranslateExerciseBinding) {
        lifecycleScope.launch {
            vm.state.map { it.isConfirm }.distinctUntilChanged().collectLatest { isConfirm ->
                if (isConfirm == false) {
                    newBinding.originalWordInputLayout.setBackgroundResource(CoreR.drawable.box_red_back)
                    newBinding.originalWordInput.setTextColor(primaryRed)
                    return@collectLatest
                }

                newBinding.originalWordInputLayout.setBackgroundResource(CoreR.drawable.box_back)
                newBinding.originalWordInput.setTextColor(primaryColor)
            }
        }
    }

    private fun handleSetImage(newBinding: FragmentWriteByImageAndTranslateExerciseBinding) {
        lifecycleScope.launch {
            vm.state.filter { it.isInited }.map { it.wordIndex }.distinctUntilChanged()
                .collectLatest { index ->
                    if (vm.state.value.let {
                            it.isActiveSubscribe and it.words[index].imageLink.isNullOrBlank().not()
                        }) {
                        setImage(
                            vm.state.value.words[index].imageLink,
                            newBinding.wordImage,
                            headerFactory.createHeaders()
                        )
                        newBinding.wordImage.visibility = View.VISIBLE
                    } else {
                        newBinding.wordImage.visibility = View.GONE
                    }
                }
        }
    }

    private fun handleSetToolBar(newBinding: FragmentWriteByImageAndTranslateExerciseBinding) {
        ToolBarController(
            title = Exercise.WORD_BY_WRITE_TRANSLATE.text,
            binding = newBinding.toolBar,
            navController = null
        ).apply {
            setDefaultSettings()
            setNavigateUp {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}