package can.lucky.of.history.domain.models.filters

import can.lucky.of.core.domain.models.filters.Queryable
import can.lucky.of.core.domain.models.filters.Range
import java.time.LocalDate

internal data class LearningHistoryFilter(
    val wordIds: Collection<String>? = null,
    val date: Range<LocalDate>? = null,
    val page: Int = 0,
    val size: Int = 20,
) : Queryable
