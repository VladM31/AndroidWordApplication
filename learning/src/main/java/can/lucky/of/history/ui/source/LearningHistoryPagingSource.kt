package can.lucky.of.history.ui.source

import can.lucky.of.core.ui.sources.AbstractPagingSource
import can.lucky.of.core.ui.sources.TemplatePageLoader
import can.lucky.of.history.domain.models.data.LearningHistory

internal typealias LearningHistoryPageLoader = TemplatePageLoader<LearningHistory>


internal class LearningHistoryPagingSource(
    pageLoader: LearningHistoryPageLoader,
    pageSize: Int
) : AbstractPagingSource<LearningHistory>(pageLoader,pageSize)