package can.lucky.of.history.domain.models.filters

import can.lucky.of.core.domain.models.filters.Queryable
import can.lucky.of.core.domain.models.filters.Range

internal data class StatisticsLearningHistoryFilter(
    val date: Range<String>? = null
) : Queryable
