package can.lucky.of.history.ui.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.domain.models.filters.Range
import can.lucky.of.core.ui.controllers.ToolBarController
import can.lucky.of.core.ui.models.ToolBarPopupButton
import can.lucky.of.history.R
import can.lucky.of.core.R as CoreR
import can.lucky.of.history.databinding.FragmentStatisticLearningHistoryBinding
import can.lucky.of.history.domain.actions.StatisticLearningHistoryAction
import can.lucky.of.history.domain.managers.LearningHistoryManager
import can.lucky.of.history.domain.models.data.StatisticsLearningHistory
import can.lucky.of.history.domain.models.enums.LearningHistoryType
import can.lucky.of.history.domain.models.filters.LearningHistoryFilter
import can.lucky.of.history.domain.models.filters.StatisticsLearningHistoryFilter
import can.lucky.of.history.domain.vms.StatisticLearningHistoryVm
import can.lucky.of.history.ui.handlers.StatisticsHandler
import can.lucky.of.history.ui.mappers.LearningHistoryTypeMapper
import can.lucky.of.history.ui.navigators.ListLearningHistoryNavigator
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class StatisticLearningHistoryFragment : Fragment(R.layout.fragment_statistic_learning_history) {
    private var binding: FragmentStatisticLearningHistoryBinding? = null
    private val vm by viewModel<StatisticLearningHistoryVm>()
    private val learningHistoryTypeMapper = LearningHistoryTypeMapper()
    private val navigator by inject<ListLearningHistoryNavigator>()


    private val statisticsHandler by lazy {
        StatisticsHandler(requireContext())
    }

    private fun nameMapper(type: LearningHistoryType): String {
        return learningHistoryTypeMapper.toUiString(type)
    }

    private fun colorMapper(type: LearningHistoryType): Int {
        return when (type) {
            LearningHistoryType.CREATE -> Color.rgb(164, 228, 251)
            LearningHistoryType.UPDATE -> Color.rgb(242, 247, 158)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentStatisticLearningHistoryBinding.bind(view)

        lifecycleScope.launch {
            vm.state.map { it.statistic }

                .collectLatest {
                    statisticsHandler.setParamsToDiagram(
                        binding?.barChart ?: return@collectLatest,
                        vm.state.value.toDate,
                        *it.toBarEntity().toTypedArray()
                    )
                }
        }

        ToolBarController(
            navController = findNavController(),
            binding = binding?.toolBar ?: return,
            title = "Statistic"
        ).apply {
            setDefaultSettings()
            setButtons(this)
        }

    }

    private fun setButtons(controller: ToolBarController) {
        val changeDateBtn = ToolBarPopupButton("Change date"){
            changeDate()
            return@ToolBarPopupButton true
        }
        val toListBtn = ToolBarPopupButton("List mode"){
            navigator.navigateToList(findNavController())
            return@ToolBarPopupButton true
        }


        controller.addContextMenu(
            CoreR.drawable.setting,
            listOf(changeDateBtn,toListBtn)
        )
    }

    private fun changeDate() {
        val listener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            vm.sent(StatisticLearningHistoryAction.SetDate(LocalDate.of(year, month + 1, dayOfMonth)))
        }

        val date = vm.state.value.toDate

        val dialog = DatePickerDialog(
            requireContext(),
            listener,
            date.year,
            date.monthValue - 1,
            date.dayOfMonth
        )

        dialog.datePicker.maxDate = LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        dialog.show()
    }

    private fun List<StatisticsLearningHistory>.toBarEntity(): List<BarDataSet> {
        return statisticsHandler.toBarEntity(StatisticLearningHistoryVm.STEP.toInt(), vm.state.value.toDate,this).map {
            BarDataSet(it.value, nameMapper(it.key)).apply {
                color = colorMapper(it.key)
                valueTextColor = statisticsHandler.primaryColor
                valueTextSize = 15f
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}