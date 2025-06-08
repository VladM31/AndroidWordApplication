package can.lucky.of.history.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.decorators.SpacesItemDecoration
import can.lucky.of.history.R
import can.lucky.of.history.databinding.FragmentListLearningHistoryBinding
import can.lucky.of.history.domain.vms.ListLearningHistoryVm
import can.lucky.of.history.ui.adapters.LearningHistoryAdapter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListLearningHistoryFragment : Fragment(R.layout.fragment_list_learning_history) {
    private var binding: FragmentListLearningHistoryBinding? = null
    private val vm by viewModel<ListLearningHistoryVm>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentListLearningHistoryBinding.bind(view)
        binding = newBinding

        val adapter = LearningHistoryAdapter()

        newBinding.recyclerHistory.layoutManager = LinearLayoutManager(requireContext())
        newBinding.recyclerHistory.adapter = adapter
        newBinding.recyclerHistory.addItemDecoration(SpacesItemDecoration(48))

        initPaging(adapter)

        ToolBarController(
            navController = findNavController(),
            binding = newBinding.toolBar,
            title = "Learning History"
        ).setDefaultSettings()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initPaging(adapter: LearningHistoryAdapter) {
        lifecycleScope.launch {
            vm.state.map { it.content }
                .distinctUntilChanged()
                .flatMapLatest { it.flow }.collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}