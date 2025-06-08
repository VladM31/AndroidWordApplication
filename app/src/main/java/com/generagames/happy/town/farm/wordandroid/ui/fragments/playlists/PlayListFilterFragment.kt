package com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.utils.onEnd
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.PlayListFilterAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPlayListFilterBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.PlayListFilterBundle
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PlayListFilterState
import com.generagames.happy.town.farm.wordandroid.domain.vms.PlayListFilterVm
import com.generagames.happy.town.farm.wordandroid.ui.navigations.PlayListFilterNavigator
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayListFilterFragment : Fragment(R.layout.fragment_play_list_filter) {
    private var binding: FragmentPlayListFilterBinding? = null
    private val vm: PlayListFilterVm by viewModel()
    private val filterNavigator by inject<PlayListFilterNavigator>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentPlayListFilterBinding.bind(view)
        binding = newBinding

        PlayListFilterFragmentArgs.fromBundle(requireArguments()).apply {
            vm.sent(PlayListFilterAction.Init(filter))
        }

        lifecycleScope.launch {
            vm.state.filter { it.isInited }.take(1).collectLatest { state ->
                newBinding.inputName.setText(state.name)
                newBinding.startCountInput.setText(state.startCount)
                newBinding.endCountInput.setText(state.endCount)

                newBinding.inputName.addTextChangedListener {
                    vm.sent(PlayListFilterAction.ChangeName(it.toString()))
                }

                newBinding.startCountInput.addTextChangedListener {
                    vm.sent(PlayListFilterAction.ChangeStartCount(it.toString()))
                }

                newBinding.endCountInput.addTextChangedListener {
                    vm.sent(PlayListFilterAction.ChangeEndCount(it.toString()))
                }
            }
        }

        newBinding.findButton.setOnClickListener {
            vm.sent(PlayListFilterAction.Find)
        }

        ToolBarController(
            binding = newBinding.toolBar,
            title = "Play list filter",
            navController = findNavController(),
            buttonImage = can.lucky.of.core.R.drawable.delete,
            buttonAction = {
                filterNavigator.popBack(this@PlayListFilterFragment, PlayListFilterBundle())
            }
        ).setDefaultSettings()

        vm.state.onEnd(lifecycleScope) {
            filterNavigator.popBack(this@PlayListFilterFragment, vm.state.toBundle())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun StateFlow<PlayListFilterState>.toBundle(): PlayListFilterBundle {
        return PlayListFilterBundle(
            startCount = value.startCount.toLongOrNull(),
            endCount = value.endCount.toLongOrNull(),
            name = value.name.ifBlank { null }
        )
    }
}