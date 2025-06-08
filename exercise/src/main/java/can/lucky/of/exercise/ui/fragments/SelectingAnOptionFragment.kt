package can.lucky.of.exercise.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import can.lucky.of.core.databinding.BoxTextContentBinding
import can.lucky.of.core.domain.factories.GlideHeaderFactory
import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.setImage
import can.lucky.of.exercise.R
import can.lucky.of.exercise.domain.actions.ExerciseAction
import can.lucky.of.exercise.domain.actions.SelectingAnOptionAction
import can.lucky.of.exercise.databinding.FragmentSelectingAnOptionBinding
import can.lucky.of.exercise.domain.models.data.ExerciseWord
import can.lucky.of.exercise.domain.models.data.ExerciseWordDetails
import can.lucky.of.exercise.domain.vm.SelectingAnOptionVm
import can.lucky.of.exercise.ui.handels.ExerciseHandle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class SelectingAnOptionFragment(
    private val exerciseType : Exercise,
    private val toText: (ExerciseWordDetails) -> String,
    private val toOption: (ExerciseWordDetails?) -> String,
    private val enableImage: Boolean = true,
    private val soundAfterAnswer: Boolean = false,
    private val soundBeforeAnswer: Boolean = false
) : Fragment(R.layout.fragment_selecting_an_option) {
    private var binding: FragmentSelectingAnOptionBinding? = null
    private val glideHeaderFactory by inject<GlideHeaderFactory>()
    private val vm by viewModel<SelectingAnOptionVm>()
    private var textContentBoxes: List<BoxTextContentBinding> = emptyList()
    private val primaryColor by lazy {
        requireContext().resources.getColor(can.lucky.of.core.R.color.primary_green, requireContext().theme)
    }
    private val primaryBack by lazy {
        requireContext().resources.getColor(can.lucky.of.core.R.color.primary_back, requireContext().theme)
    }
    private val primaryRed by lazy {
        requireContext().resources.getColor(can.lucky.of.core.R.color.primary_red, requireContext().theme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentSelectingAnOptionBinding.bind(view)
        binding = newBinding

        if (vm.state.value.isInited.not()){
            (requireActivity() as? ExerciseHandle)?.state?.let { state ->
                vm.sent(SelectingAnOptionAction.Init(
                    state.words,
                    exerciseType,
                    state.transactionId,
                    state.isActiveSubscribe,
                    soundBeforeAnswer,
                    soundAfterAnswer
                ))
            }
        }

        initTextBoxes(newBinding)

        handleSetToolBar()
        handleWordsToChoose()
        handleSetOriginal()
        handleSetImage()
        handleNext()
        handleAnswer()

        handleEnd()


        lifecycleScope.launch {
            vm.state.map { it.isCorrect }
                .distinctUntilChanged()
                .filter { it == null }
                .collectLatest { _ ->
                    textContentBoxes.forEach { box ->
                        box.root.setBackgroundResource(can.lucky.of.core.R.drawable.box_back)
                        box.content.setTextColor(primaryColor)
                    }
                }

        }
    }

    private fun initTextBoxes(newBinding: FragmentSelectingAnOptionBinding) {
        textContentBoxes = List(ELEMENTS_COUNT.coerceAtMost(vm.state.value.wordsToChoose.size)) {
            BoxTextContentBinding.inflate(layoutInflater, newBinding.root, false)
        }
        textContentBoxes.forEach { newBinding.contentLayout.addView(it.root) }
    }

    private fun handleEnd() {
        val handle = requireActivity() as? ExerciseHandle

        lifecycleScope.launch {
            vm.state.filter { it.isEnd and it.waitNext.not() }.distinctUntilChanged()
                .collectLatest {
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
    }

    private fun handleAnswer(){
        lifecycleScope.launch{
            vm.state.map { it.isCorrect }.distinctUntilChanged().filterNotNull().collectLatest { isCorrect ->
                val wordId = vm.state.value.currentWord().wordId

                textContentBoxes.forEach { box ->
                    if (box.root.tag == wordId){
                        box.root.setBackgroundResource(can.lucky.of.core.R.drawable.button_back)
                        box.content.setTextColor(primaryBack)
                    }
                }

                if (isCorrect.not()){
                    textContentBoxes.forEach { entry ->
                        if (entry.root.tag != wordId){
                            entry.root.setBackgroundResource(can.lucky.of.core.R.drawable.box_red_back)
                            entry.content.setTextColor(primaryRed)
                        }
                    }
                }
            }
        }
    }

    private fun handleClick(wordId: String){
        if (vm.state.value.waitNext.not()) {
            vm.sent(SelectingAnOptionAction.ChooseWord(wordId))
        }
    }

    private fun handleNext(){
        binding?.nextButton?.setOnClickListener {
            vm.sent(SelectingAnOptionAction.Next)
        }


        lifecycleScope.launch {
            vm.state.map { it.waitNext }
                .distinctUntilChanged()
                .collectLatest {
                    binding?.nextButton?.run {
                        isEnabled = it
                        visibility = if (it) View.VISIBLE else View.GONE
                    }
                }
        }

    }

    private fun handleWordsToChoose(){
        lifecycleScope.launch {
            vm.state.map { it.wordsToChoose }
                .filter { it.isNotEmpty() }
                .distinctUntilChanged().collectLatest { wordsToChoose ->
                    textContentBoxes.forEachIndexed { index, box ->
                        box.content.text = wordsToChoose.getOrNull(index).let(toOption)
                        box.root.tag = wordsToChoose.getOrNull(index)?.wordId
                        box.root.setOnClickListener {
                            handleClick(wordsToChoose.getOrNull(index)?.wordId.toString())
                        }
                    }
                }
        }
    }

    private fun handleSetImage(){
        if (enableImage.not()){
            binding?.wordImage?.visibility = View.GONE
            return
        }

        lifecycleScope.launch{
            vm.state.filter { it.words.isNotEmpty() && it.isInited  }
                .map { it.currentWord() }
                .distinctUntilChanged()
                .collectLatest { word ->
                    if (word.imageLink.isNullOrEmpty() || vm.state.value.isActiveSubscribe.not()){
                        binding?.wordImage?.visibility = View.GONE
                        return@collectLatest
                    }

                    binding?.wordImage?.visibility = View.VISIBLE
                    setImage(word.imageLink,binding?.wordImage, glideHeaderFactory.createHeaders())
                }
        }
    }

    private fun handleSetOriginal(){
        lifecycleScope.launch {
            vm.state.filter { it.words.isNotEmpty() && it.isInited  }
                .map { it.currentWord() }
                .distinctUntilChanged()
                .collectLatest { word ->
                    binding?.translateWord?.text = toText(word)
                }
        }
    }

    private fun handleSetToolBar(){
        ToolBarController(
            title = exerciseType.text,
            binding = binding?.toolBar ?: return,
            navController = null
        ).apply {
            setDefaultSettings()
            setNavigateUp{
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        textContentBoxes = emptyList()
    }

    private companion object {
        private const val ELEMENTS_COUNT = 3
    }
}