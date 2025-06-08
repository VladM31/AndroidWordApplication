package can.lucky.of.core.domain.models.enums

import can.lucky.of.core.utils.titleCase

enum class  WordSortBy {
    ORIGIN,
    TRANSLATE,
    LANG,
    TRANSLATE_LANG,
    CATEGORY;

    val titleCase = this.titleCase()

    companion object{
        private val mapTitleCase = entries.associateBy { it.titleCase }

        fun fromTitleCase(value: String): WordSortBy? {
            return mapTitleCase[value]
        }
    }
}