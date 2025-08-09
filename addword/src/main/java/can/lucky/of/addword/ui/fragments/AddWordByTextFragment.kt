package can.lucky.of.addword.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import can.lucky.of.addword.R
import can.lucky.of.addword.databinding.FragmentAddWordByTextBinding
import can.lucky.of.addword.domain.actions.AddWordByImageAction
import can.lucky.of.addword.domain.actions.AddWordByTextAction
import can.lucky.of.addword.domain.models.keys.RecognizeWordTaskKeys
import can.lucky.of.addword.domain.vms.AddWordByTextVm
import can.lucky.of.addword.ui.listeners.ItemSelectedListener
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.addDebounceAfterTextChangedListener
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import can.lucky.of.core.utils.setContent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddWordByTextFragment : Fragment(R.layout.fragment_add_word_by_text)   {
    private var binding: FragmentAddWordByTextBinding? = null
    private val vm by viewModel<AddWordByTextVm>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddWordByTextBinding.bind(view)

        setValues()

        setUiListeners()

        setStateListeners()

        ToolBarController(
            findNavController(),
            binding?.toolBar ?: return,
            "Recognize by Text",
        ).setDefaultSettings()

        vm.state.onEnd(lifecycleScope) {
            parentFragmentManager.setFragmentResult(RecognizeWordTaskKeys.REQUEST_CODE, bundleOf())
            findNavController().navigateUp()
        }
    }

    private fun setStateListeners() {
        vm.state.onError(lifecycleScope) {
            requireActivity().showError(it.message).show()
        }

        lifecycleScope.launch {
            vm.state.map { it.isLoading }.distinctUntilChanged().collectLatest {
                binding?.addWordButton?.isEnabled = it.not()
                binding?.addWordButton?.visibility = if (it) View.INVISIBLE else View.VISIBLE
                binding?.loading?.root?.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }
    }

    private fun setUiListeners() {
        binding?.inputWord?.addDebounceAfterTextChangedListener(100) {
            vm.sent(AddWordByTextAction.SetOriginal(it))
        }

        binding?.inputTranslate?.addDebounceAfterTextChangedListener(100) {
            vm.sent(AddWordByTextAction.SetTranslate(it))
        }

        binding?.spinnerWordLang?.onItemSelectedListener = ItemSelectedListener<String> { title ->
            vm.sent(AddWordByTextAction.SetLanguage(Language.fromTitleCase(title)))
        }

        binding?.spinnerTranLang?.onItemSelectedListener = ItemSelectedListener<String> { title ->
            vm.sent(AddWordByTextAction.SetTranslateLanguage(Language.fromTitleCase(title)))
        }

        binding?.addWordButton?.setOnClickListener {
            vm.sent(AddWordByTextAction.Recognize)
        }
    }

    private fun setValues() {
        binding?.inputWord?.setText(vm.state.value.original)
        binding?.inputTranslate?.setText(vm.state.value.translate)
        val langs = Language.entries.filter { it.isDefined }.map { it.titleCase }.toTypedArray()

        binding?.spinnerWordLang?.setContent(langs)
        binding?.spinnerTranLang?.setContent(langs)

        binding?.spinnerWordLang?.setSelection(Language.entries.indexOf(vm.state.value.language))
        binding?.spinnerTranLang?.setSelection(Language.entries.indexOf(vm.state.value.translateLang))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}