package com.generagames.happy.town.farm.wordandroid.ui.fragments.words

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.WordSortBy
import can.lucky.of.core.domain.models.filters.WordFilter
import can.lucky.of.core.ui.controllers.LangCheckBoxController
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.WordFilterAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentWordFilterBinding
import com.generagames.happy.town.farm.wordandroid.utils.mappers.toWordFilter
import com.generagames.happy.town.farm.wordandroid.domain.models.keys.WordFilterKeys
import com.generagames.happy.town.farm.wordandroid.domain.models.states.WordFilterState
import com.generagames.happy.town.farm.wordandroid.ui.controllers.MultiInputController
import com.generagames.happy.town.farm.wordandroid.ui.controllers.SortByController
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.controllers.TypedCheckBoxController
import can.lucky.of.core.ui.models.ToolBarPopupButton
import can.lucky.of.core.utils.addDebounceAfterTextChangedListener
import can.lucky.of.core.utils.createLangCheckBox
import can.lucky.of.core.utils.dp
import com.generagames.happy.town.farm.wordandroid.domain.vms.WordFilterViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.core.R as CoreR


class WordFilterFragment : Fragment(R.layout.fragment_word_filter) {
    private var binding: FragmentWordFilterBinding? = null
    private val filterViewModel: WordFilterViewModel by viewModel()
    private var categoryController : MultiInputController? = null
    private var langController : LangCheckBoxController? = null
    private var translateLangController: LangCheckBoxController? = null
    private var cefrController: TypedCheckBoxController<CEFR>? = null
    private val gson = Gson()

    val value: WordFilterState
        get() = filterViewModel.state.value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentWordFilterBinding.bind(view)
        binding = newBinding

        WordFilterFragmentArgs.fromBundle(requireArguments()).filter.let {
            filterViewModel.sent(WordFilterAction.Init(gson.fromJson(it, WordFilter::class.java)))
        }

        lifecycleScope.launch {
            filterViewModel.state.take(1).collect{ state ->
                initWordLangCheckBox(newBinding)
                initTranslateLangCheckBox(newBinding)
                initMultiInputController(newBinding)
                initSortByController(newBinding)
                initToolBarController(newBinding)
                initListeners(newBinding)
                initValues(newBinding, state)
                initCefrCheckBox(newBinding, state.cefrs?.firstOrNull())
            }
        }
    }

    private fun initWordLangCheckBox(binding : FragmentWordFilterBinding ){
       langController = createLangCheckBox(binding.wordLanguageLayout, 150.dp,
            startValue = value.originalLang ?: Language.UNDEFINED ) {
            filterViewModel.sent(
                WordFilterAction.SetOriginalLang(
                    if (it == Language.UNDEFINED) {
                        null
                    } else {
                        it
                    }
                )
            )
        }
    }

    private fun initCefrCheckBox(binding : FragmentWordFilterBinding, cefr: CEFR? ){
        cefrController = TypedCheckBoxController(
            startValue = cefr,
            values = CEFR.entries.toList(),
            viewGroup = binding.cefrLayout,
            width = 100.dp
        ){
            filterViewModel.sent(WordFilterAction.SetCefr(it))
        }
    }

    private fun initTranslateLangCheckBox(binding : FragmentWordFilterBinding ){
        translateLangController = createLangCheckBox(binding.translateLanguageLayout, 150.dp,
            startValue = value.translateLang ?: Language.UNDEFINED) {
            filterViewModel.sent(
                WordFilterAction.SetTranslateLang(
                    if (it == Language.UNDEFINED) {
                        null
                    } else {
                        it
                    }
                )
            )
        }
    }

    private fun initMultiInputController(binding : FragmentWordFilterBinding ){
        categoryController = MultiInputController(
            binding = binding.categoryInput,
            title = getString(CoreR.string.category),
            startValues = value.categories ?: emptyList(),
        ) {
            filterViewModel.sent(WordFilterAction.SetCategories(it.ifEmpty { null }))
        }
    }

    private fun initSortByController(binding : FragmentWordFilterBinding){
        val objects = WordSortBy.entries.map { it.titleCase }.filter { it != value.sortBy?.titleCase }


        SortByController(
            binding = binding.sortBy,
            adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_text_view,
                value.sortBy?.titleCase?.let {
                    listOf(it) + objects
                } ?: run {
                    objects
                }
            ),
            onChangeSortBy = { value ->
                filterViewModel.sent(WordFilterAction.SetSortBy(value?.let { sort ->
                    WordSortBy.fromTitleCase(
                        sort
                    )
                }))
            },
            onChangeDirection = { value ->
                filterViewModel.sent(WordFilterAction.SetAsc(value))
            },
            initDirection = value.asc,
        )
    }

    private fun initToolBarController(binding : FragmentWordFilterBinding){
        ToolBarController(
            navController = findNavController(),
            binding = binding.toolBar,
            title = getString(CoreR.string.word_filter),
            buttonImage = CoreR.drawable.delete,
            buttonAction = {
                clear()
            }
        ).setDefaultSettings()
    }

    private fun initListeners(binding : FragmentWordFilterBinding){
        binding.inputWord.addDebounceAfterTextChangedListener(200){
            filterViewModel.sent(WordFilterAction.SetOriginal(it.ifEmpty { null }))
        }

        binding.inputTranslate.addDebounceAfterTextChangedListener(200){
            filterViewModel.sent(WordFilterAction.SetTranslate(it.ifEmpty { null }))
        }

        binding.findButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(WordFilterKeys.REQUEST_CODE,toFilterBundle())
            findNavController().navigateUp()
        }
    }

    private fun initValues(binding : FragmentWordFilterBinding, state: WordFilterState){
        binding.inputWord.setText(state.original)
        binding.inputTranslate.setText(state.translate)
    }

    private fun toFilterBundle() : Bundle {
        return bundleOf(WordFilterKeys.FILTER to gson.toJson(value.toWordFilter()))
    }

    private fun clear(){
        categoryController?.clear()
        binding?.inputWord?.setText("")
        binding?.inputTranslate?.setText("")
        langController?.clear()
        translateLangController?.clear()
        cefrController?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        categoryController = null
        langController = null
        translateLangController = null
        cefrController = null
    }

}