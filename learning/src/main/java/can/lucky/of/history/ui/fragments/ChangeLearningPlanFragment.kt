package can.lucky.of.history.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.utils.onEnd
import can.lucky.of.core.utils.onError
import can.lucky.of.core.utils.onSelect
import can.lucky.of.core.utils.setContent
import can.lucky.of.history.R
import can.lucky.of.history.databinding.FragmentChangeLearningPlanBinding
import can.lucky.of.history.domain.models.enums.PlanFragmentType
import can.lucky.of.history.domain.vms.ChangeLearningPlanVm
import can.lucky.of.history.ui.navigators.LearningPlanNavigator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import can.lucky.of.history.domain.actions.ChangeLearningPlanAction
import can.lucky.of.core.utils.setOnProgressChanged
import can.lucky.of.core.utils.titleCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.take

class ChangeLearningPlanFragment : Fragment(R.layout.fragment_change_learning_plan) {
    private var binding: FragmentChangeLearningPlanBinding? = null
    private val vm by viewModel<ChangeLearningPlanVm>()
    private val navigator by inject<LearningPlanNavigator>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChangeLearningPlanBinding.bind(view)

        initToolbar()

        if (vm.state.value.isInit.not()) {
            val bundle = navigator.getBundle(this)
            vm.sent(ChangeLearningPlanAction.Init(bundle))
        }

        handleAfterInit()

        handleProgress()

        vm.state.onEnd(lifecycleScope){
            navigator.popBackStack(findNavController(),this, parentFragmentManager)
        }

        vm.state.onError(lifecycleScope){
            requireActivity().showError(it.message).show()
        }

        lifecycleScope.launch {
            vm.state.map { it.type }.distinctUntilChanged()
                .collectLatest {
                    binding?.submitButton?.text = it.titleCase()
                }
        }

        setHandleSubmit()
    }

    private fun setHandleSubmit() {
        binding?.submitButton?.setOnClickListener {
            binding?.submitButton?.isEnabled = false
            binding?.submitButton?.isClickable = false

            vm.sent(ChangeLearningPlanAction.Submit)

            lifecycleScope.launch {
                delay(2000)
                binding?.submitButton?.isEnabled = true
                binding?.submitButton?.isClickable = true
            }
        }
    }

    private fun handleProgress() {
        lifecycleScope.launch {
            vm.state.filter { it.isInit }.map { it.wordsPerDay }.distinctUntilChanged()
                .collectLatest {
                    binding?.wordCount?.text = it.toString()
                    binding?.wordsPerDaySeekBar?.progress = it
                }
        }
    }

    private fun handleAfterInit() {
        lifecycleScope.launch {
            vm.state.filter { it.isInit }
                .take(1)
                .collectLatest { state ->
                    binding?.wordsPerDaySeekBar?.setOnProgressChanged {
                        vm.sent(ChangeLearningPlanAction.ChangeWordsPerDay(it))
                    }
                    binding?.wordsPerDaySeekBar?.progress = state.wordsPerDay

                    binding?.minusCount?.setOnClickListener {
                        vm.sent(ChangeLearningPlanAction.MinusOneWordsPerDay)
                    }

                    binding?.plusCount?.setOnClickListener {
                        vm.sent(ChangeLearningPlanAction.PlusOneWordsPerDay)
                    }

                    val langs = Language.entries.filter { it.isDefined }

                    binding?.nativeLangSpinner?.setContent(langs.map { it.titleCase })
                    binding?.nativeLangSpinner?.setSelection(langs.indexOfFirst { it == state.nativeLang })
                    binding?.nativeLangSpinner?.onSelect<String> {
                        vm.sent(ChangeLearningPlanAction.ChangeNativeLang(it.orEmpty()))
                    }

                    binding?.learningLangSpinner?.setContent(langs.map { it.titleCase })
                    binding?.learningLangSpinner?.setSelection(langs.indexOfFirst { it == state.learningLang })
                    binding?.learningLangSpinner?.onSelect<String> {
                        vm.sent(ChangeLearningPlanAction.ChangeLearningLang(it.orEmpty()))
                    }

                    binding?.cefrLevelSpinner?.setContent(CEFR.entries)
                    binding?.cefrLevelSpinner?.setSelection(CEFR.entries.indexOfFirst { it == state.cefr })
                    binding?.cefrLevelSpinner?.onSelect<CEFR> {
                        it?.let {
                            vm.sent(ChangeLearningPlanAction.ChangeCefr(it))
                        }
                    }
                }
        }
    }


    private fun initToolbar() {
        val toolbar = binding?.toolBar ?: return
        val controller = ToolBarController(
            binding = toolbar,
            title = "Change Learning Plan",
            navController = findNavController()
        ).apply {
            setDefaultSettings()
        }

        lifecycleScope.launch {
            vm.state.map { it.type }
                .filter { it != PlanFragmentType.UNDEFINED }
                .distinctUntilChanged()
                .collectLatest {
                    val title = when (it) {
                        PlanFragmentType.CREATE -> "Create learning plan"
                        PlanFragmentType.EDIT -> "Edit learning plan"
                        else -> "Change Learning Plan"
                    }
                    controller.setTitle(title)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}