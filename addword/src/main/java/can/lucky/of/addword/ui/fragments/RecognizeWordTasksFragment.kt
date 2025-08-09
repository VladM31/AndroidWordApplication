package can.lucky.of.addword.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.addword.R
import can.lucky.of.addword.databinding.FragmentAnalyzeWordTasksBinding
import can.lucky.of.addword.domain.actions.RecognizeWordTasksAction
import can.lucky.of.addword.domain.models.AiRecognizeResult
import can.lucky.of.addword.domain.models.keys.RecognizeWordTaskKeys
import can.lucky.of.addword.domain.vms.RecognizeWordTasksVm
import can.lucky.of.addword.ui.adapters.AiRecognizeResultAdapter
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.onError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR

class RecognizeWordTasksFragment : Fragment(R.layout.fragment_analyze_word_tasks) {
    private var binding: FragmentAnalyzeWordTasksBinding? = null

    private val vm by viewModel<RecognizeWordTasksVm>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentAnalyzeWordTasksBinding.bind(view)
        binding = newBinding

        initPagination()
        initToolBar()
        initOpenListener()
        setOtherListeners()


    }

    private fun setOtherListeners() {
        vm.state.onError(lifecycleScope) {
            requireActivity().showError(it.message).show()
        }

        parentFragmentManager.setFragmentResultListener(
            RecognizeWordTaskKeys.REQUEST_CODE,
            viewLifecycleOwner
        ) { _, _ ->
            vm.sent(RecognizeWordTasksAction.Reload)
        }
    }

    private fun initOpenListener() {
        lifecycleScope.launch {
            vm.state.map { it.word }
                .distinctUntilChanged()
                .drop(1)
                .filterNotNull()
                .collectLatest { word ->
                    if (isAdded && findNavController().currentDestination?.id == R.id.analyzeWordTasksFragment) {
                        findNavController().navigate(
                            RecognizeWordTasksFragmentDirections.actionAnalyzeWordTasksFragmentToDefaultAddWordFragment(
                                word
                            )
                        )
                    }

                }
        }
    }

    private fun initPagination() {
        val adapter = AiRecognizeResultAdapter {
            vm.sent(RecognizeWordTasksAction.OpenRecognizeResult(it.recognizeId))
        }
        binding?.resultsRecyclerView?.adapter = adapter
        binding?.resultsRecyclerView?.layoutManager = LinearLayoutManager(requireContext())


        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state
                    .map { it.pagingData }
                    .filterNotNull()
                    .distinctUntilChanged { old, new -> old === new }
                    .collectLatest { pagingData ->
                        adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    }
            }
        }

        lifecycleScope.launch {
            vm.state.map { it.isLoading }
                .distinctUntilChanged()
                .filter { !it }
                .collectLatest {
                    binding?.loading?.root?.visibility = View.INVISIBLE
                }
        }

        binding?.swipeRefreshLayout?.setOnRefreshListener {
            vm.sent(RecognizeWordTasksAction.Reload)
            binding?.swipeRefreshLayout?.isRefreshing = false
        }
    }


    private fun initToolBar() {
        val toolBar = binding?.toolBar ?: return
        ToolBarController(
            findNavController(),
            toolBar,
            "Recognize results",
            CoreR.drawable.add
        ) {
            navigateToRecognizePanel()
        }.setDefaultSettings()
    }

    private fun navigateToRecognizePanel() {
        findNavController().navigate(
            RecognizeWordTasksFragmentDirections.actionAnalyzeWordTasksFragmentToAddWordByTextFragment()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.resultsRecyclerView?.adapter = null
        binding = null
    }


}