package can.lucky.of.exercise.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.exercise.R
import can.lucky.of.exercise.domain.actions.ExerciseSelectAction
import can.lucky.of.exercise.databinding.FragmentExerciseSelectionBinding
import can.lucky.of.exercise.domain.vm.ExerciseSelectionVm
import can.lucky.of.exercise.ui.adapters.ExerciseAdapter
import can.lucky.of.exercise.ui.controllers.ExerciseSelectionController


import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExerciseSelectionFragment : Fragment(R.layout.fragment_exercise_selection) {
    private var binding: FragmentExerciseSelectionBinding? = null
    private val exerciseSelectionVm by viewModel<ExerciseSelectionVm>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = ExerciseSelectionFragmentArgs.fromBundle(requireArguments())
        val newBinding = FragmentExerciseSelectionBinding.bind(view)
        binding = newBinding

        val adapter = ExerciseAdapter(
            controller = ExerciseSelectionController(exerciseSelectionVm),
            context = requireContext()
        )
        newBinding.exerciseRecyclerView.adapter = adapter
        newBinding.exerciseRecyclerView.layoutManager = LinearLayoutManager(context)

        lifecycleScope.launch {
            exerciseSelectionVm.state.map { it.number }.distinctUntilChanged().collectLatest {
                adapter.notifyDataChanged()
            }
        }

        lifecycleScope.launch {
            exerciseSelectionVm.state.map { it.errorMessage }
                .drop(1)
                .distinctUntilChanged()
                .filter { it.message.isNotBlank() }.shareIn(
                lifecycleScope,
                SharingStarted.WhileSubscribed(),
                0
            ).collectLatest {
              Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            exerciseSelectionVm.state.map { it.isConfirmed }.distinctUntilChanged().filter { it }.collectLatest {
                val options = NavOptions.Builder().setPopUpTo(R.id.exerciseSelectionFragment, true).build()
                val transactionId = exerciseSelectionVm.state.value.transactionId
                findNavController().navigate(
                    ExerciseSelectionFragmentDirections.actionExerciseSelectionFragmentToExerciseActivity(
                        transactionId,
                        bundle.playListId
                    ),options)
                findNavController().popBackStack(R.id.exerciseSelectionFragment, true)
            }
        }

        ToolBarController(findNavController(),newBinding.toolBar, "Select exercises").setDefaultSettings()

        newBinding.confirmButton.setOnClickListener {
            exerciseSelectionVm.sent(ExerciseSelectAction.ConfirmSelection)
        }

        bundle.let {
            exerciseSelectionVm.sent(ExerciseSelectAction.SetTransactionId(it.transactionId))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}