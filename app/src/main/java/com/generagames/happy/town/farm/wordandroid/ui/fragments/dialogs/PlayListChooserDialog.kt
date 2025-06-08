package com.generagames.happy.town.farm.wordandroid.ui.fragments.dialogs

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.core.ui.decorators.SpacesItemDecoration
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.PlayListChooserDialogAction
import com.generagames.happy.town.farm.wordandroid.ui.adapters.PlayListChooserAdapter
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPlayListChooserBinding
import com.generagames.happy.town.farm.wordandroid.domain.vms.PlayListChooserDialogVm
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListChooserDialog : DialogFragment(R.layout.fragment_play_list_chooser) {
    private var binding: FragmentPlayListChooserBinding? = null
    private val viewModel by viewModel<PlayListChooserDialogVm>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlayListChooserBinding.bind(view)

        val adapter = createChooserAdapter()

        binding?.content?.layoutManager = LinearLayoutManager(requireContext())
        binding?.content?.adapter = adapter
        binding?.content?.addItemDecoration(SpacesItemDecoration(14))

        val playListFlow = getPlayListFlow()
        lifecycleScope.launch {
            playListFlow.collectLatest {
                adapter.submitData(it)
            }
        }

        binding?.applyButton?.setOnClickListener {
            handleApplyClick()
        }

        binding?.cancelButton?.setOnClickListener {
            dismiss()
        }
    }

    private fun handleApplyClick() {
        parentFragmentManager.setFragmentResult(
            REQUEST_CODE,
            bundleOf(
                KEY_RESPONSE to viewModel.state.value.selectedPlayListId
            )
        )
        dismiss()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getPlayListFlow() = viewModel.state
        .map { it.pagerPlayList }
        .distinctUntilChanged()
        .flatMapLatest { it.flow }
        .cachedIn(viewModel.viewModelScope)

    private fun createChooserAdapter() = PlayListChooserAdapter(
        isChose = { id -> viewModel.state.value.selectedPlayListId == id },
        onChoose = { id, position ->
            val prev = viewModel.state.value.previousPosition
            viewModel.sent(PlayListChooserDialogAction.SelectPlayList(id, position))
            prev?.let {
                binding?.content?.adapter?.notifyItemChanged(it)
            }
        },
    )

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object{
        @JvmStatic
        val TAG: String = PlayListChooserDialog::class.java.simpleName

        val REQUEST_CODE = TAG + "_REQUEST_CODE"
        val KEY_RESPONSE = TAG + "_KEY_RESPONSE"

    }
}