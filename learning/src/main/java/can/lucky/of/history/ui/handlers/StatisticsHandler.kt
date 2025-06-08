package can.lucky.of.history.ui.handlers

import android.content.Context
import can.lucky.of.core.R
import can.lucky.of.history.domain.models.data.StatisticsLearningHistory
import can.lucky.of.history.domain.models.enums.LearningHistoryType
import can.lucky.of.history.domain.vms.StatisticLearningHistoryVm
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

internal class StatisticsHandler(
    context: Context
) {
    private val formatter = DateTimeFormatter.ofPattern("MM-dd")

    val primaryColor =  context.getColor(R.color.primary_green)

    internal fun toBarEntity(step: Int, dateValue: LocalDate,statistics: List<StatisticsLearningHistory>) : Map<LearningHistoryType,List<BarEntry>>{
        val localDateNow = dateValue.minusDays(step.toLong())

        val created = LinkedHashMap<LocalDate, Int>()
        val updated = LinkedHashMap<LocalDate, Int>()

        repeat(step){
            val date = localDateNow.plusDays(it.toLong() +1)
            created[date] = 0
            updated[date] = 0
        }

        statistics.forEach {
            when(it.type){
                LearningHistoryType.CREATE -> created[it.date] = it.count
                LearningHistoryType.UPDATE -> updated[it.date] = it.count
            }
        }

       return mapOf(
            LearningHistoryType.CREATE to created.values.mapIndexed { index, i -> BarEntry(index.toFloat(), i.toFloat()) },
            LearningHistoryType.UPDATE to updated.values.mapIndexed { index, i -> BarEntry(index.toFloat(), i.toFloat()) }
        )
    }

    internal fun setParamsToDiagram(barChart: BarChart, date: LocalDate,vararg dataSets: BarDataSet) {
        val barData = BarData(dataSets.toList())

        val groupSpace = 0.06f // было 0.08f
        val barSpace = 0.02f // было 0.03f
        val barWidth = 0.45f // увеличьте, чтобы заполнить пространство (должно быть меньше, чтобы сумма с barSpace была < 1)


        barData.barWidth = barWidth
        barChart.data = barData

        val dateStep = date.minusDays(StatisticLearningHistoryVm.STEP)


        // Specify the bar order and group them together
        val xAxis = barChart.xAxis
        xAxis.granularity = 1f
        xAxis.isGranularityEnabled = true
        xAxis.setCenterAxisLabels(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = IndexAxisValueFormatter(List(StatisticLearningHistoryVm.STEP.toInt() ) { i ->
            dateStep.plusDays(i.toLong() + 1).format(formatter)
        })
        val leftAxis = barChart.axisLeft
        leftAxis.axisMinimum = 0f // Start at zero
        xAxis.textSize = 15f
        barChart.axisLeft.textSize = 15f


        barChart.axisRight.isEnabled = false // No right axis

        barChart.description.isEnabled = false // No description label

        val groupWidth = groupSpace + (barSpace + barWidth) * 2
        // Group the data
        barChart.xAxis.axisMinimum = 0f
        barChart.xAxis.axisMaximum = 0 + groupWidth * 4
        barChart.groupBars(0f, groupSpace, barSpace)

        // Change the text color for the X-axis labels
        barChart.xAxis.textColor = primaryColor // use your desired color

        // Change the text color for the Y-axis (left) labels
        barChart.axisLeft.textColor = primaryColor // use your desired color

        // Change the text color for the Y-axis (right) labels, if enabled
        barChart.axisRight.textColor = primaryColor // use your desired color4

        barChart.legend.textColor = primaryColor // use your desired color
        barChart.legend.textSize = 20f
        barChart.legend.formSize = 18f
        barChart.legend.xEntrySpace = 50f


        barChart.invalidate() // Refresh the chart
    }
}