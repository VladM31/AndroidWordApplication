package com.generagames.happy.town.farm.wordandroid.ui.fragments.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import can.lucky.of.core.domain.models.filters.WordFilter
import can.lucky.of.core.ui.controllers.ToolBarController
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.WordsAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentWordsBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.keys.WordFilterKeys
import com.generagames.happy.town.farm.wordandroid.domain.vms.WordsViewModel
import com.generagames.happy.town.farm.wordandroid.ui.adapters.WordAdapter
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR

class WordsFragment : Fragment(R.layout.fragment_words) {
    private val viewModel by viewModel<WordsViewModel>()
    private var binding: FragmentWordsBinding? = null
    private val gson = Gson()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentWordsBinding.bind(view)
        binding = newBinding

        val adapter = createAdapter()

        newBinding.bottomToolBar.delete.visibility = View.GONE
        newBinding.bottomToolBar.root.visibility = View.GONE
        newBinding.wordsRecyclerView.adapter = adapter
        newBinding.wordsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        initWordFilter(newBinding)
        initCollectBottomToolBarVisibility(newBinding)
        initPaging(adapter)
        initCanselClick(newBinding)
        initToolBarController(newBinding)

        newBinding.bottomToolBar.ok.setOnClickListener {
            if (viewModel.state.value.selectedWords.keys.isEmpty()){
                return@setOnClickListener
            }
            findNavController().navigate(
                WordsFragmentDirections.actionWordsFragmentToPinUserWordsFragment(
                    viewModel.state.value.selectedWords.keys.toTypedArray()
                )
            )
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initCollectBottomToolBarVisibility(newBinding : FragmentWordsBinding){
        lifecycleScope.launch {
            viewModel.state
                .map { it.selectedWords }
                .distinctUntilChanged()
                .collect { selectedWords ->
                newBinding.bottomToolBar.root.visibility = if (selectedWords.isNotEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
                    newBinding.bottomToolBar.ok.text = "Add (${selectedWords.size})"
            }
        }
    }

    private fun clearSelectedWords(newBinding : FragmentWordsBinding){
        val temp = viewModel.state.value.selectedWords
        viewModel.sent(WordsAction.Clear)
        val itemCount = newBinding.wordsRecyclerView.adapter?.itemCount ?: 0
        temp.values.filter { it < itemCount }.forEach { position ->
            newBinding.wordsRecyclerView.adapter?.runCatching {
                notifyItemChanged(position)
            }
        }
    }

    private fun initCanselClick(newBinding : FragmentWordsBinding){
        newBinding.bottomToolBar.cancel.setOnClickListener {
            clearSelectedWords(newBinding)
        }
    }

    private fun createAdapter() : WordAdapter {
        return WordAdapter(viewModel) {
            findNavController().navigate(
                WordsFragmentDirections.actionWordsFragmentToWordFragment(
                    it
                )
            )
        }
    }

    private fun initPaging(adapter: WordAdapter){
        lifecycleScope.launch {
            viewModel.state.value.words.collectLatest  {
                adapter.submitData(it)
            }
        }
    }

    private fun initToolBarController(newBinding : FragmentWordsBinding){
        ToolBarController(
            navController = findNavController(),
            binding = newBinding.toolBar,
            title = getString(CoreR.string.words),
            buttonImage = CoreR.drawable.find,
            buttonAction = {
                findNavController().navigate(
                    WordsFragmentDirections.actionWordsFragmentToWordFilterFragment(
                        gson.toJson(viewModel.state.value.filter)
                    )
                )
            }
        ).setDefaultSettings()
    }

    private fun initWordFilter(newBinding : FragmentWordsBinding){
        parentFragmentManager.setFragmentResultListener(WordFilterKeys.REQUEST_CODE,this){
                _, bundle ->
            val filter = gson.runCatching {
                fromJson(bundle.getString(WordFilterKeys.FILTER) ?: "{}",WordFilter::class.java)
            }.getOrElse { WordFilter() }

            if (filter != viewModel.state.value.filter) {
                val action = WordsAction.UpdateFilter(filter, viewModel.state.value.selectedWords)
                clearSelectedWords(newBinding)
                viewModel.sent(action)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

