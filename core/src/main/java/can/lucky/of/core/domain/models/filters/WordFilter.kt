package can.lucky.of.core.domain.models.filters

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.WordSortBy

data class WordFilter(
    val originalLang: Language? = null,
    val translateLang: Language? = null,
    val original: String? = null,
    val translate: String? = null,
    val categories: Collection<String>? = null,
    val wordIds: Collection<String>? = null,
    val cefrs: Collection<CEFR>? = null,
    val userId: UserId? = null,
    val pagination: PageFilter<WordSortBy>? = null
) : Queryable {

    data class UserId(
        val isIn: Boolean = false,
        val id: String? = null
    )
}