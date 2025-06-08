package can.lucky.of.history.ui.mappers

import can.lucky.of.history.domain.models.enums.LearningHistoryType

class LearningHistoryTypeMapper {

    internal fun toUiString(type: LearningHistoryType): String {
        return when (type) {
            LearningHistoryType.CREATE -> "Added"
            LearningHistoryType.UPDATE -> "Leaning"
        }
    }
}