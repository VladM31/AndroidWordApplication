package com.generagames.happy.town.farm.wordandroid.ui.fragments.words

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.domain.factories.GlideHeaderFactory
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.models.data.words.UserWordDetails
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.enums.Language
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.WordAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentWordBinding
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.models.ToolBarPopupButton
import can.lucky.of.core.ui.navigators.WordRemoveListener
import can.lucky.of.core.utils.setImage
import can.lucky.of.core.utils.setSound

import com.generagames.happy.town.farm.wordandroid.domain.vms.WordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.utils.titleCase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.take
import can.lucky.of.core.R as CoreR


class WordFragment : Fragment(R.layout.fragment_word) {
    private var binding: FragmentWordBinding? = null
    private val headerFactory = get<GlideHeaderFactory>()
    private val subscribeCacheManager: SubscribeCacheManager by inject()
    private val wordRemoveListener by inject<WordRemoveListener>()
    private val wordViewModel by viewModel<WordViewModel>()
    private val categoryTextTemplate by lazy { getString(CoreR.string.category_template) }
    private val textTemplate by lazy { getString(CoreR.string.text_template) }
    private val lastDateReadTemplate by lazy { getString(CoreR.string.last_date_read_template) }
    private val dateOfAddTemplate by lazy { getString(CoreR.string.date_of_add_template) }
    private val cefrLevelTemplate by lazy { getString(R.string.cefr_level) }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentWordBinding.bind(view)
        binding = newBinding

        setToolBar()

        if (wordViewModel.state.value.word == null) {
            val args = WordFragmentArgs.fromBundle(requireArguments())
            wordViewModel.sent(WordAction.Init(args.word, args.details))
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val isActive = subscribeCacheManager.isActiveSubscribe()

            withContext(Dispatchers.Main) {
                wordViewModel.state.map { it.word }.distinctUntilChanged().collect { word ->
                    if (word == null) return@collect

                    handleSubscribeState(isActive = isActive, word = word)
                    handleWord(word = word)
                }
            }
        }

        lifecycleScope.launch {
            wordViewModel.state.map { it.userWordDetails }.distinctUntilChanged()
                .collect { details ->
                    handleDetails(details = details)
                }
        }

        lifecycleScope.launch {
            wordViewModel.state.map { it.isDeleted }
                .distinctUntilChanged()
                .filter { it }
                .take(1)
                .collect {
                    wordRemoveListener.invokeWordRemoved(parentFragmentManager)
                    findNavController().navigateUp()
                }
        }

        lifecycleScope.launch {
            wordViewModel.state.map { it.word?.description }.
            distinctUntilChanged()
                .filterNotNull().collect { description ->
                binding?.descriptionText?.text = "Definition: $description"
            }
        }
    }

    private fun handleDetails(details: UserWordDetails?) {
        val visibility = if (details == null) View.GONE else View.VISIBLE

        binding?.apply {
            dateOfAddText.visibility = visibility
            lastDateReadText.visibility = visibility
        }

        binding?.apply {
            details?.let { details ->
                dateOfAddText.text = dateOfAddTemplate.format(details.dateOfAdded)
                lastDateReadText.text = lastDateReadTemplate.format(details.lastReadDate)
            }
        }
    }

    private fun handleWord(word: Word) {
        binding?.apply {
            categoryText.text = categoryTextTemplate.format(word.category.orEmpty())
            wordText.text = textTemplate.format(
                Language.fromShortName(word.lang).titleCase(), word.original
            )
            translateText.text = textTemplate.format(
                Language.fromShortName(word.translateLang).titleCase(), word.translate
            )
            cefrLevelText.text = cefrLevelTemplate.format(word.cefr)
        }
    }

    private fun handleSubscribeState(isActive: Boolean, word: Word) {
        if (isActive) {
            setImage(word.imageLink, binding?.image, headerFactory.createHeaders())
            binding?.soundButton?.let {
                setSound(it, word.soundLink, headerFactory.createHeaders().headers)
            }
            return
        }

        binding?.image?.visibility = View.GONE
        binding?.imageSpace?.visibility = View.GONE

        binding?.soundButton?.setImageResource(
            if (word.soundLink != null) {
                CoreR.drawable.sound_disable
            } else {
                CoreR.drawable.sound_empty
            }
        )

        return
    }


    private fun setToolBar() {
        val toolBar = binding?.toolBar ?: return

        val delete = ToolBarPopupButton(title = "Delete") {
            this.wordViewModel.sent(WordAction.Delete)
            true
        }

        val shareBtn = ToolBarPopupButton(title = "Share") {
            findNavController().navigate(
                WordFragmentDirections.actionWordFragmentToShareUserWordFragment(
                    userWordId = wordViewModel.state.value.userWordDetails?.userWordId.orEmpty()
                )
            )
            true
        }

        val controller = ToolBarController(
            navController = findNavController(),
            binding = toolBar,
            title = getString(CoreR.string.word_details),
        ).apply {
            setDefaultSettings()
        }

        lifecycleScope.launch {
            wordViewModel.state.map { it.userWordDetails }
                .distinctUntilChanged()
                .filterNotNull()
                .collectLatest {
                    controller.addContextMenu(CoreR.drawable.setting, delete, shareBtn)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}