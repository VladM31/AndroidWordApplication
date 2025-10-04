package com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists

import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.core.domain.models.filters.PlayListCountFilter
import can.lucky.of.core.domain.models.filters.Range
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.PlayListAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPlayListBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.PlayListFilterBundle
import com.generagames.happy.town.farm.wordandroid.domain.models.data.NavigateBarButton
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PlayListState
import com.generagames.happy.town.farm.wordandroid.domain.vms.PlayListViewModel
import com.generagames.happy.town.farm.wordandroid.ui.adapters.PlayListCountAdapter
import com.generagames.happy.town.farm.wordandroid.ui.controllers.NavigateBarController
import com.generagames.happy.town.farm.wordandroid.ui.navigations.PlayListFilterNavigator
import com.generagames.happy.town.farm.wordandroid.ui.navigations.ReFetchPlayListsNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayListFragment : Fragment(R.layout.fragment_play_list) {
    private var binding: FragmentPlayListBinding? = null
    private val listViewModel by viewModel<PlayListViewModel>()
    private val filterNavigator by inject<PlayListFilterNavigator>()
    private val navigator by inject<ReFetchPlayListsNavigator>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val newBinding = FragmentPlayListBinding.bind(view)
        binding = newBinding

        newBinding.playListContainer.layoutManager = LinearLayoutManager(requireContext())
        val adapter = PlayListCountAdapter { playListId ->
            PlayListFragmentDirections.actionPlayListFragmentToOpenDetails(playListId).apply {
                findNavController().navigate(this)
            }
        }
        newBinding.playListContainer.adapter = adapter

        handlePaging(adapter)

        initAddPlayList()
        handleResult()

        NavigateBarController(

            binding = newBinding.navigationBar,
            navController = findNavController(),
            button = NavigateBarButton.PLAY_LIST
        ).start()

        newBinding.findButton.setOnClickListener {
            filterNavigator.navigateToFilter(findNavController(), listViewModel.state.toBundle())
        }

        filterNavigator.listenBack(this) {
            listViewModel.sent(PlayListAction.UpdateFilter(it.toFilter()))
        }
    }

    private fun initAddPlayList(){
        val popup = PopupMenu(ContextThemeWrapper(requireActivity(), can.lucky.of.core.R.style.CustomPopupMenu), binding?.addPlayListButton)

        popup.menu.add("Create").setOnMenuItemClickListener{
            findNavController().navigate(PlayListFragmentDirections.actionPlayListFragmentToCreatePlayListFragmentDialog())
            true
        }

//        popup.menu.add("Scan QrCode").setOnMenuItemClickListener {
//            findNavController().navigate(PlayListFragmentDirections.actionPlayListFragmentToScanPlayListFragment())
//            true
//        }

        binding?.addPlayListButton?.setOnClickListener {
            popup.show()
        }
    }

    private fun StateFlow<PlayListState>.toBundle(): PlayListFilterBundle {
        return PlayListFilterBundle(
            startCount = value.filter.count?.from,
            endCount = value.filter.count?.to,
            name = value.filter.name
        )
    }

    private fun PlayListFilterBundle.toFilter(): PlayListCountFilter {
        return PlayListCountFilter(
            count = Range(
                from = startCount,
                to = endCount
            ),
            name = name
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun handlePaging(adapter: PlayListCountAdapter) {
        lifecycleScope.launch {
            listViewModel.state
                .flatMapLatest { it.pager.flow }
                .cachedIn(listViewModel.viewModelScope)
                .collectLatest {
                    adapter.submitData(it)
                }
        }
    }

    private fun handleResult() {
        navigator.listenRequest(parentFragmentManager,this){
            listViewModel.sent(PlayListAction.ReFetch)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}