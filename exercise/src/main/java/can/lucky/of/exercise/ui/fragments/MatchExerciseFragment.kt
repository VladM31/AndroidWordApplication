package can.lucky.of.exercise.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.core.domain.models.enums.Exercise
import can.lucky.of.exercise.R
import can.lucky.of.exercise.domain.actions.CompareExerciseAction
import can.lucky.of.exercise.domain.actions.ExerciseAction
import can.lucky.of.exercise.databinding.FragmentCompareExerciseBinding
import can.lucky.of.exercise.domain.models.data.ExerciseWord
import can.lucky.of.exercise.domain.vm.MatchExerciseVm
import can.lucky.of.exercise.ui.adapters.CompareExerciseWordAdapter
import can.lucky.of.exercise.ui.handels.ExerciseHandle
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.onEnd

class MatchExerciseFragment : Fragment(R.layout.fragment_compare_exercise) {
    private var binding: FragmentCompareExerciseBinding? = null
    private val vm by viewModel<MatchExerciseVm>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentCompareExerciseBinding.bind(view)
        binding = newBinding

        if (vm.state.value.isInited.not()){
            (requireActivity() as? ExerciseHandle)?.state?.let { state ->
                vm.sent(CompareExerciseAction.Init(state.words, state.transactionId))
            }
        }

        val originalAdapter = CompareExerciseWordAdapter(
            isOriginal = true,
            context = requireContext(),
            isEnable = { vm.state.value.original == null }){ id, index ->
            vm.sent(CompareExerciseAction.Click(isOriginal = true, wordId = id, index))
        }

        val translateAdapter = CompareExerciseWordAdapter(isOriginal = false,
            context = requireContext(),
            isEnable = { vm.state.value.translate == null }) { id, index ->
            vm.sent(CompareExerciseAction.Click(isOriginal = false, wordId = id, index))
        }

        newBinding.originWords.adapter = originalAdapter
        newBinding.originWords.layoutManager = LinearLayoutManager(requireContext())
        newBinding.translateWords.adapter = translateAdapter
        newBinding.translateWords.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            vm.state.map { it.originalWords }.distinctUntilChanged().collect {
                originalAdapter.notifyDataChanged(it)
            }
        }

        lifecycleScope.launch {
            vm.state.map { it.translateWords }.distinctUntilChanged().collect {
                translateAdapter.notifyDataChanged(it)
            }
        }

        handleSetToolBar(newBinding)
        handleEnd()
    }

    private fun handleEnd(){
        vm.state.onEnd(lifecycleScope){
            (requireActivity() as? ExerciseHandle)?.sent(
                ExerciseAction.NextExercise(
                vm.state.value.words.map { ExerciseWord(
                    userWordId = it.userWordId,
                    grade = it.grade + 1,
                    transactionId = it.transactionId
                ) }
            ))
        }
    }

    private fun handleSetToolBar(newBinding : FragmentCompareExerciseBinding){
        ToolBarController(
            title = Exercise.COMPARE.text,
            binding = newBinding.toolBar,
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
    }
}