package com.generagames.happy.town.farm.wordandroid.ui.fragments.words

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.UserWordSortBy
import can.lucky.of.core.domain.models.enums.WordSortBy
import can.lucky.of.core.ui.controllers.LangCheckBoxController
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.controllers.TypedCheckBoxController
import can.lucky.of.core.utils.addDebounceAfterTextChangedListener
import can.lucky.of.core.utils.createLangCheckBox
import can.lucky.of.core.utils.createTypedCheckBox
import can.lucky.of.core.utils.dp
import com.generagames.happy.town.farm.wordandroid.R
import com.generagames.happy.town.farm.wordandroid.actions.UserWordFilterAction
import com.generagames.happy.town.farm.wordandroid.actions.WordFilterAction
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentUserWordFilterBinding
import com.generagames.happy.town.farm.wordandroid.databinding.FragmentWordFilterBinding
import com.generagames.happy.town.farm.wordandroid.domain.models.bundles.UserWordFilterBundle
import com.generagames.happy.town.farm.wordandroid.domain.models.states.UserWordFilterState
import com.generagames.happy.town.farm.wordandroid.domain.vms.UserWordFilterVm
import com.generagames.happy.town.farm.wordandroid.ui.controllers.MultiInputController
import com.generagames.happy.town.farm.wordandroid.ui.controllers.SortByController
import com.generagames.happy.town.farm.wordandroid.ui.navigations.UserWordFilterNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserWordFilterFragment : Fragment(R.layout.fragment_user_word_filter) {
    private var binding: FragmentUserWordFilterBinding? = null
    private var categoryController : MultiInputController? = null
    private var langController : LangCheckBoxController? = null
    private var translateLangController: LangCheckBoxController? = null
    private var cefrController: TypedCheckBoxController<CEFR>? = null
    private val vm by viewModel<UserWordFilterVm>()
    private val navigator by inject<UserWordFilterNavigator>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val newBinding = FragmentUserWordFilterBinding.bind(view)
        binding = newBinding

        if (vm.state.value.isInit.not()){
            vm.sent(UserWordFilterAction.Init(navigator.getBundle(this)))
        }

        lifecycleScope.launch {
            vm.state.filter { it.isInit }.take(1).collectLatest { state ->
                initSortByController(newBinding)
                newBinding.inputWord.setText(state.original.orEmpty())
                newBinding.inputWord.addDebounceAfterTextChangedListener(200) { text ->
                    vm.sent(UserWordFilterAction.SetOriginal(text))
                }
                newBinding.inputTranslate.setText(state.translate.orEmpty())
                newBinding.inputTranslate.addDebounceAfterTextChangedListener(200) { text ->
                    vm.sent(UserWordFilterAction.SetTranslate(text))
                }
                initCategoryInputController(newBinding)
                langController = initLangCheckBox(newBinding.wordLanguageLayout, state.lang ?: Language.UNDEFINED) {
                    vm.sent(UserWordFilterAction.SetLang(it))
                }
                translateLangController = initLangCheckBox(newBinding.translateLanguageLayout, state.translateLang ?: Language.UNDEFINED) {
                    vm.sent(UserWordFilterAction.SetTranslateLang(it))
                }
                initCefrCheckBox(state.cefr) {
                    vm.sent(UserWordFilterAction.SetCefr(it))
                }
            }
        }


        newBinding.findButton.setOnClickListener {
            navigator.popBack(findNavController(), parentFragmentManager, vm.state.value.toBundle())
        }

        initToolBar(newBinding)
    }

    private fun initToolBar(newBinding: FragmentUserWordFilterBinding) {
        ToolBarController(
            binding = newBinding.toolBar,
            title = "My word filter",
            navController = findNavController(),
            buttonImage = can.lucky.of.core.R.drawable.delete,
            buttonAction = { onClear()}
        ).setDefaultSettings()
    }

    private fun initLangCheckBox(viewGroup: ViewGroup, initValue: Language, onChoose: (lang: Language) -> Unit) : LangCheckBoxController {
        return createLangCheckBox(viewGroup, 150.dp, startValue = initValue, onClick = onChoose)
    }

    private fun initCefrCheckBox(initValue: CEFR?, onChoose: (lang: CEFR?) -> Unit)  {
        val layout = binding?.cefrLayout ?: return
        cefrController =  createTypedCheckBox(layout, 100.dp, startValue = initValue, values = CEFR.entries.toList(), onClick = onChoose)
    }

    private fun initCategoryInputController(binding : FragmentUserWordFilterBinding ){
        categoryController = MultiInputController(
            binding = binding.categoryInput,
            title = getString(can.lucky.of.core.R.string.category),
            startValues = vm.state.value.categories ?: emptyList(),
        ) {
            vm.sent(UserWordFilterAction.SetCategories(it))
        }
    }

    private fun initSortByController(binding : FragmentUserWordFilterBinding){
        val sorts = UserWordSortBy.entries.map { it.titleCase }

        SortByController(
            binding = binding.sortBy,
            adapter = ArrayAdapter(requireContext(), R.layout.spinner_text_view, sorts),
            onChangeSortBy = { value -> vm.sent(UserWordFilterAction.SetSortBy(value.orEmpty())) },
            onChangeDirection = { value -> vm.sent(UserWordFilterAction.SetAsc(value)) },
            initDirection = vm.state.value.asc,
        )
    }

    private fun UserWordFilterState.toBundle() : UserWordFilterBundle {
        return UserWordFilterBundle(
            original = original,
            translate = translate,
            originalLang = lang?.titleCase,
            translateLang = translateLang?.titleCase,
            categories = categories,
            sortBy = sortBy,
            asc = asc,
            cefr = cefr
        )
    }

    private fun onClear(){
        categoryController?.clear()
        binding?.inputWord?.setText("")
        binding?.inputTranslate?.setText("")
        translateLangController?.clear()
        langController?.clear()
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