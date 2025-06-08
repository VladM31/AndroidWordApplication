package com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.EditPlayListAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentEditPlayListBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.EditPlayListBundle
import com.generagames.happy.town.farm.wordandroid.domain.models.states.EditPlayListState
import com.generagames.happy.town.farm.wordandroid.domain.vms.EditPlayListVm
import com.generagames.happy.town.farm.wordandroid.ui.navigations.EditPlayListNavigator
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlayListFragment : Fragment(R.layout.fragment_edit_play_list) {
    private var binding: FragmentEditPlayListBinding? = null
    private val vm by viewModel<EditPlayListVm>()
    private val navigator by inject<EditPlayListNavigator>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentEditPlayListBinding.bind(view)
        binding = newBinding


        val args = EditPlayListFragmentArgs.fromBundle(requireArguments()).editParams.apply {
            vm.sent(EditPlayListAction.Init(playListId, name))
        }

        newBinding.playListNameInput.setText(vm.state.value.name.ifEmpty { args.name })

        newBinding.playListNameInput.addTextChangedListener {
            vm.sent(EditPlayListAction.NameChanged(it.toString()))
        }

        newBinding.submitBtn.setOnClickListener {
            vm.sent(EditPlayListAction.Submit)
        }

        lifecycleScope.launch {
            vm.state.map { it.isSubmitEnabled }.collectLatest {
                newBinding.submitBtn.isEnabled = it
            }
        }

        vm.state.onEnd(lifecycleScope) {
            navigator.popBack(findNavController(), parentFragmentManager, vm.state.toEditBundle())
        }

        vm.state.onError(lifecycleScope) {
            with(requireContext()) {
                showError(it.message).show()
            }
        }

        ToolBarController(
            binding = newBinding.toolbar,
            title = "Edit Play List",
            navController = findNavController()
        ).setDefaultSettings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun StateFlow<EditPlayListState>.toEditBundle(): EditPlayListBundle {
        return EditPlayListBundle(
            playListId = value.playListId,
            name = value.name
        )
    }
}