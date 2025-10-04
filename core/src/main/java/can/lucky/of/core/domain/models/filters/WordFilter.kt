package can.lucky.of.core.domain.models.filters

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.WordSortBy
import can.lucky.of.core.domain.models.enums.WordType

data class WordFilter(
    val wordIds: Collection<String>? = null,
    val languages: Set<Language>? = null,
    val translateLanguages: Set<Language>? = null,
    val categories: Collection<String>? = null,
    val cefrs: Collection<CEFR>? = null,
    val types: Collection<WordType>? = null,

    val original: String? = null,
    val translate: String? = null,
    val userId: UserId? = null,

    val sortField: WordSortBy = WordSortBy.ORIGIN,
    val asc: Boolean = false,
    val page: Int = 0,
    val size: Int = 20,
) : Queryable {

    data class UserId(
        val isIn: Boolean = false,
        val id: String? = null
    )
}