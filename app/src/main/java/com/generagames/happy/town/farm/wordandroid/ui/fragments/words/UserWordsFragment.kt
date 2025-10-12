package com.generagames.happy.town.farm.wordandroid.ui.fragments.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.core.domain.models.data.words.UserWordDetails
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.filters.UserWordFilter
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.decorators.SpacesItemDecoration
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.UserWordsAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentWordsBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.UserWordFilterBundle
import com.generagames.happy.town.farm.wordandroid.domain.models.keys.WordFilterKeys
import com.generagames.happy.town.farm.wordandroid.domain.vms.UserWordsViewModel
import com.generagames.happy.town.farm.wordandroid.ui.adapters.UserWordAdapter
import com.generagames.happy.town.farm.wordandroid.ui.fragments.dialogs.PlayListChooserDialog
import com.generagames.happy.town.farm.wordandroid.ui.fragments.playlists.PlayListFragmentDirections
import com.generagames.happy.town.farm.wordandroid.ui.navigations.UserWordFilterNavigator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR


class UserWordsFragment : Fragment(R.layout.fragment_words) {
    private var binding: FragmentWordsBinding? = null
    private val title by lazy { getString(CoreR.string.user_word_title_template) }
    private val viewModel by viewModel<UserWordsViewModel>()
    private val navigator by inject<UserWordFilterNavigator>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentWordsBinding.bind(view)
        binding = newBinding

        newBinding.wordsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        newBinding.wordsRecyclerView.adapter = createAdapter().also {
            initPaging(it)
        }
        newBinding.wordsRecyclerView.addItemDecoration(SpacesItemDecoration(40))

        initBottomBar()
        initToolBarController(newBinding)
        initDialogResult()
        initReFetchListener()

        handleTitle()
        handleSelectedWords()
        handleOpenPlayList()

        navigator.listenBundle(parentFragmentManager, this) {
            viewModel.sent(UserWordsAction.ChangeFilter(it.toFilter()))
        }
    }

    private fun handleOpenPlayList(){
        lifecycleScope.launch {
            viewModel.state.map { it.openPlayList }
                .filter { it != null }
                .distinctUntilChanged()
                .filter { findNavController().currentDestination?.id == R.id.userWordsFragment }
                .map { it?.id }
                .filterNotNull()
                .collectLatest { playListId ->
                    findNavController().popBackStack(R.id.menuFragment, false)
                    findNavController().navigate(R.id.play_list_nav_graph)
                    PlayListFragmentDirections.actionPlayListFragmentToOpenDetails(playListId).apply {
                        findNavController().navigate(this)
                    }
            }
        }
    }


    private fun initDialogResult(){
        parentFragmentManager.setFragmentResultListener(PlayListChooserDialog.REQUEST_CODE, viewLifecycleOwner){
            _, bundle ->
            bundle.getString(PlayListChooserDialog.KEY_RESPONSE)?.let {
                viewModel.sent(UserWordsAction.PinWords(it))
            }
        }
    }

    private fun initReFetchListener(){
        parentFragmentManager.setFragmentResultListener(WordFilterKeys.RE_FETCH_CODE, viewLifecycleOwner){ _, _ ->
            viewModel.sent(UserWordsAction.ReFetch)
        }
    }

    private fun handleTitle() {
        lifecycleScope.launch {
            viewModel.state.map { it.count }.distinctUntilChanged().collectLatest {
                binding?.toolBar?.title?.text = title.format(it)
            }
        }
    }

    private fun createAdapter(): UserWordAdapter {
        return UserWordAdapter(onDetails = {
                val action = UserWordsFragmentDirections.actionUserWordsFragmentToWordFragment(
                    it.word,
                    UserWordDetails.from(it)
                )
                findNavController().navigate(action)
            },
            isSelected = {
                viewModel.state.value.selectedWords.contains(it)
            },
            selectWord = { id, position ->
                if (viewModel.state.value.selectedWords.contains(id)) {
                    viewModel.sent(UserWordsAction.UnSelectWord(id))
                } else {
                    position?.let {
                        viewModel.sent(UserWordsAction.SelectWord(id, it))
                    }
                }
            })
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initPaging(adapter: UserWordAdapter) {
        lifecycleScope.launch {
            viewModel.state.map { it.pager }
                .distinctUntilChanged()
                .flatMapLatest { it.flow }.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    private fun UserWordFilterBundle.toFilter(): UserWordFilter {
        return UserWordFilter(
            languages = originalLang?.let { setOf(Language.fromTitleCase(it)) } ?: emptySet(),
            original = original,
            translateLanguages = translateLang?.let { setOf(Language.fromTitleCase(it)) }
                ?: setOf(),
            translate = translate,
            categories = categories,
            sortField = sortBy,
            asc = asc,
            cefrs = cefr?.let { listOf(it) }
        )
    }

    private fun initToolBarController(newBinding: FragmentWordsBinding) {
        ToolBarController(
            navController = findNavController(),
            binding = newBinding.toolBar,
            title = title.format(viewModel.state.value.count),
            buttonImage = CoreR.drawable.find,
            buttonAction = {
                navigator.navigateToFilter(findNavController(), viewModel.state.value.filter.toBundle())
            }
        ).setDefaultSettings()
    }

    private fun UserWordFilter.toBundle(): UserWordFilterBundle {
        return UserWordFilterBundle(
            originalLang = languages?.firstOrNull()?.titleCase,
            original = original,
            translateLang = translateLanguages?.firstOrNull()?.titleCase,
            translate = translate,
            categories = categories?.toList(),
            sortBy = sortField,
            asc = false,
            cefr = cefrs?.firstOrNull()
        )
    }

    @SuppressLint("SetTextI18n")
    private fun handleSelectedWords() {
        lifecycleScope.launch {
            viewModel.state
                .map { it.selectedWords }
                .distinctUntilChanged()
                .collect { selectedWords ->
                    binding?.bottomToolBar?.root?.visibility = if (selectedWords.isNotEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                    binding?.bottomToolBar?.ok?.text = "OK (${selectedWords.size})"
                }
        }
    }

    private fun onClearSelectedWords(){
        val selectedWords = viewModel.state.value.selectedWords.values

        viewModel.sent(UserWordsAction.Clear)

        selectedWords.forEach { position ->
            binding?.wordsRecyclerView?.adapter?.notifyItemChanged(position)
        }
    }

    private fun initBottomBar(){
        binding?.bottomToolBar?.delete?.visibility = View.GONE

        binding?.bottomToolBar?.cancel?.setOnClickListener {
            onClearSelectedWords()
        }

        binding?.bottomToolBar?.ok?.setOnClickListener {
            findNavController().navigate(UserWordsFragmentDirections.actionUserWordsFragmentToPlayListChooserDialog())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}