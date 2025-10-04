package com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.core.domain.models.data.words.UserWordDetails
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.models.ToolBarPopupButton
import can.lucky.of.core.ui.navigators.WordRemoveListener
import can.lucky.of.core.utils.onEnd
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.PlayListDetailsAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentPlayListDetailsBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.EditPlayListBundle
import com.generagames.happy.town.farm.wordandroid.domain.models.states.PlayListDetailsState
import com.generagames.happy.town.farm.wordandroid.domain.vms.PlayListDetailsVm
import com.generagames.happy.town.farm.wordandroid.ui.adapters.PinnedWordAdapter
import com.generagames.happy.town.farm.wordandroid.ui.navigations.EditPlayListNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR


class PlayListDetailsFragment : Fragment(R.layout.fragment_play_list_details) {
    private var binding: FragmentPlayListDetailsBinding? = null
    private val viewModel: PlayListDetailsVm by viewModel()
    private val wordCountTemplate by lazy { getString(CoreR.string.words_count_template) }
    private val wordRemoveListener by inject<WordRemoveListener>()

    private val navigator by inject<EditPlayListNavigator>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentPlayListDetailsBinding.bind(view)
        binding = newBinding

        PlayListDetailsFragmentArgs.fromBundle(requireArguments()).let {
            viewModel.sent(PlayListDetailsAction.Fetch(it.playListId))
        }

        newBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        launchWords(newBinding)
        val toolBarController = initToolBar(newBinding)

        lifecycleScope.launch {
            viewModel.state.map { it.name }.distinctUntilChanged().collectLatest {
                toolBarController.setTitle(it)
            }
        }

        lifecycleScope.launch {
            viewModel.state.map { it.selectedWords }.distinctUntilChanged().collectLatest {
                newBinding.wordsCount.text =
                    wordCountTemplate.format(if (it.isEmpty()) "all" else it.size)
            }
        }

        viewModel.state.onEnd(lifecycleScope) {
            findNavController().popBackStack()
        }

        lifecycleScope.launch {
            viewModel.state.map { it.transactionId }.distinctUntilChanged().drop(1).filterNotNull().shareIn(
                lifecycleScope,
                SharingStarted.WhileSubscribed(),
                0
            ).collectLatest {
                if (findNavController().currentBackStackEntry?.destination?.id != R.id.playListDetailsFragment) {
                    return@collectLatest
                }

                findNavController().navigate(R.id.action_playListDetailsFragment_to_exerscise_nav_graph, bundleOf(
                    "transactionId" to it,
                    "playListId" to viewModel.state.value.id
                ))
                delay(500)
                newBinding.playButton.isClickable = true
            }
        }

        newBinding.unselectButton.setOnClickListener {
            handleUnSelect()
        }

        newBinding.unPinButton.setOnClickListener {
            viewModel.sent(PlayListDetailsAction.UnPin)
        }

        newBinding.playButton.setOnClickListener {
          newBinding.playButton.isClickable = false
          viewModel.sent(PlayListDetailsAction.StartTransaction)
        }

        with(navigator){
            listenBack {
                viewModel.sent(PlayListDetailsAction.HandleEdit(it.name))
            }
        }

        wordRemoveListener.onWordRemove(this, parentFragmentManager) {
            viewModel.sent(PlayListDetailsAction.ReFetch)
        }
    }

    private fun initToolBar(newBinding: FragmentPlayListDetailsBinding) : ToolBarController {
//        val share = ToolBarPopupButton("Share"){
//            findNavController().navigate(
//                PlayListDetailsFragmentDirections.actionPlayListDetailsFragmentToSharePlayListFragment(
//                    viewModel.state.value.id
//                )
//            )
//            return@ToolBarPopupButton true
//        }

        val edit = ToolBarPopupButton("Edit"){
            navigator.navigateToEdit(findNavController(), viewModel.state.toEditBundle())
            return@ToolBarPopupButton true
        }

        val remove = ToolBarPopupButton("Remove"){
            viewModel.sent(PlayListDetailsAction.Delete)
            return@ToolBarPopupButton true
        }

        return ToolBarController(
            binding = newBinding.toolBar,
            navController = findNavController(),
            title = viewModel.state.value.name
        ).apply {
            setDefaultSettings()
            addContextMenu(CoreR.drawable.setting, edit, remove)
        }
    }

    private fun launchWords(newBinding: FragmentPlayListDetailsBinding) {
        lifecycleScope.launch {
            viewModel.state.map { it.words }.distinctUntilChanged()
                .collectLatest {
                    newBinding.recyclerView.adapter = PinnedWordAdapter(
                        words = it,
                        isSelected = { id -> viewModel.state.value.selectedWords.containsKey(id) },
                        selectWord = { id, position ->
                            viewModel.sent(PlayListDetailsAction.SelectWord(id, position))
                        },
                        openWord = { userWord ->
                            PlayListDetailsFragmentDirections.actionPlayListDetailsFragmentToWordFragment(
                                userWord.word,
                                UserWordDetails.from(userWord)
                            ).apply {
                                    findNavController().navigate(this)
                                }
                        }
                    )
                }
        }
    }

    private fun handleUnSelect() {
        val selectedWords = viewModel.state.value.selectedWords

        if (selectedWords.isEmpty()) return

        viewModel.sent(PlayListDetailsAction.UnSelect)

        selectedWords.values.forEach { position ->
            binding?.recyclerView?.adapter?.notifyItemChanged(
                position
            )
        }
    }

    private fun StateFlow<PlayListDetailsState>.toEditBundle() : EditPlayListBundle{
        return EditPlayListBundle(
            name = value.name,
            playListId = value.id
        )
    }
}







