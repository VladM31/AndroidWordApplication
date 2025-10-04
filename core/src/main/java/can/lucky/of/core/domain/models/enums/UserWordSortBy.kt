package can.lucky.of.core.domain.models.enums

import can.lucky.of.core.utils.titleCase

enum class UserWordSortBy {
    LANG,
    ORIGIN,
    TRANSLATE,
    CATEGORY,
    CREATED_AT,
    LAST_READ_DATE,
    TRANSLATE_LANG;

    val titleCase = this.titleCase()

    companion object{
        private val mapTitleCase = UserWordSortBy.entries.associateBy { it.titleCase }

        fun fromTitleCase(value: String): UserWordSortBy? {
            return mapTitleCase[value]
        }
    }
}