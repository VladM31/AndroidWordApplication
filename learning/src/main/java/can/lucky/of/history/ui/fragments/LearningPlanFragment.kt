package can.lucky.of.history.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.dialogs.showError
import can.lucky.of.core.ui.models.ToolBarPopupButton
import can.lucky.of.core.utils.onError
import can.lucky.of.core.utils.titleCase
import can.lucky.of.history.R
import can.lucky.of.history.databinding.FragmentLearningPlanBinding
import can.lucky.of.history.domain.actions.LearningPlanAction
import can.lucky.of.history.domain.models.bundles.LearningPlanBundle
import can.lucky.of.history.domain.models.enums.PlanFragmentType
import can.lucky.of.history.domain.vms.LearningPlanVm
import can.lucky.of.history.ui.navigators.LearningPlanNavigator
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class LearningPlanFragment : Fragment(R.layout.fragment_learning_plan) {
    private var binding: FragmentLearningPlanBinding? = null
    private val vm by viewModel<LearningPlanVm>()
    private val navigator: LearningPlanNavigator by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLearningPlanBinding.bind(view)

        binding?.createPlanButton?.setOnClickListener {
            navigator.navigateToChangePlan(findNavController(), bundle = vm.toBundle())
        }

        navigator.setListener(this, parentFragmentManager) {
            vm.sent(LearningPlanAction.ReFetchPlan)
        }

        vm.state.onError(lifecycleScope){
            requireActivity().showError(it.message).show()
        }

        handleType()
        handleUpdatePlan()
        handleToolBar()
    }

    @SuppressLint("SetTextI18n")
    private fun handleUpdatePlan() {
        lifecycleScope.launch {
            vm.state.map { it.learningPlan }.filterNotNull()
                .distinctUntilChanged()
                .collectLatest { plan ->
                    binding?.detailsLayout?.wordsLearnedToday?.text =
                        vm.state.value.learnedWordsToDay.toString() + " / " + plan.wordsPerDay
                    binding?.detailsLayout?.addedWords?.text = vm.state.value.addedWords.toString()
                    binding?.detailsLayout?.learningWords?.text =
                        vm.state.value.learnedWords.toString()
                    binding?.detailsLayout?.cefrLevel?.text = plan.cefr.toString()
                    binding?.detailsLayout?.nativeLanguage?.text = plan.nativeLang.titleCase()
                    binding?.detailsLayout?.learningLanguage?.text = plan.learningLang.titleCase()
                    setDiagram()
                }
        }
    }

    private fun handleToolBar(){
        val toolBarController = ToolBarController(
            binding = binding?.toolBar ?: return,
            title = "Learning Plan",
            navController = findNavController()
        )
        toolBarController.setDefaultSettings()

        val btn = ToolBarPopupButton(
            title = "Change plan",
            menuItemClickListener = {
                navigator.navigateToChangePlan(findNavController(), bundle = vm.toBundle())
                true
            }
        )

        lifecycleScope.launch {
            vm.state.map { it.learningPlan }
                .filterNotNull()
                .take(1)
                .collectLatest {
                    toolBarController.addContextMenu(can.lucky.of.core.R.drawable.setting, listOf(btn))
                }
        }
    }


    private fun handleType() {
        lifecycleScope.launch {
            vm.state.map { it.type }
                .filter { it != PlanFragmentType.UNDEFINED }
                .distinctUntilChanged()
                .collectLatest {
                    binding?.loadingLayout?.root?.visibility = View.INVISIBLE
                    when (it) {
                        PlanFragmentType.CREATE -> {
                            binding?.detailsLayout?.root?.visibility = View.INVISIBLE
                            binding?.createPlanButton?.visibility = View.VISIBLE
                            binding?.createPlanButton?.isClickable = true
                            binding?.createPlanButton?.isEnabled = true
                        }

                        PlanFragmentType.EDIT -> {
                            binding?.createPlanButton?.visibility = View.INVISIBLE
                            binding?.detailsLayout?.root?.visibility = View.VISIBLE
                            binding?.createPlanButton?.isClickable = false
                            binding?.createPlanButton?.isEnabled = false
                        }

                        PlanFragmentType.UNDEFINED -> {}
                    }
                }
        }
    }

    private fun setDiagram(){
        val primaryGreen = requireActivity().getColor(can.lucky.of.core.R.color.primary_green)
        val primaryBack = requireActivity().getColor(can.lucky.of.core.R.color.primary_back)
        val need : Float = vm.state.value.learningPlan?.wordsPerDay?.toFloat() ?: 0f
        val learned = vm.state.value.learnedWordsToDay.toFloat()

        var notLearnedPercent = (need - learned) / need * 100f
        if (notLearnedPercent < 0) notLearnedPercent = 0f
        val learnedPercent = 100f - notLearnedPercent.coerceAtMost(100f)

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(notLearnedPercent, "Not learned"))
        entries.add(PieEntry(learnedPercent, "Learned"))

        val colors = ArrayList<Int>()
        colors.add(Color.RED)
        colors.add(Color.GREEN)

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.valueTextSize = 18f

        binding?.detailsLayout?.pieChart?.data = PieData(dataSet)

        val pieChart = binding?.detailsLayout?.pieChart ?: return

        pieChart.isDrawHoleEnabled = true // Отключаем центральное отверстие
        pieChart.setEntryLabelTextSize(0f) // Размер текста для подписей сегментов
        pieChart.description.isEnabled = false; // Отключаем описание диаграммы
        pieChart.legend.isEnabled = true
        pieChart.legend.textColor = primaryGreen
        pieChart.legend.textSize = 14f
        pieChart.legend.formSize = 10f
        pieChart.legend.xEntrySpace = 35f
        pieChart.legend.yEntrySpace = 5f
        pieChart.legend.isWordWrapEnabled = true
        pieChart.animateY(500);

        pieChart.centerText = "Progress"
        pieChart.setCenterTextColor(primaryGreen)
        pieChart.setCenterTextSize(16f)
        pieChart.setHoleColor(primaryBack)

        pieChart.minOffset = 0f
    }

    private fun LearningPlanVm.toBundle() = LearningPlanBundle(
        type = state.value.type,
        wordsPerDay = state.value.learningPlan?.wordsPerDay ?: 1,
        nativeLang = state.value.learningPlan?.nativeLang ?: Language.UNDEFINED,
        learningLang = state.value.learningPlan?.learningLang ?: Language.UNDEFINED,
        cefr = state.value.learningPlan?.cefr ?: CEFR.A1
    )

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}