package can.lucky.of.core.domain.models.filters

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.UserWordSortBy
import can.lucky.of.core.domain.models.enums.WordType


data class UserWordFilter(
    val userWordIds: Collection<String>? = null,
    val wordIds: Collection<String>? = null,

    val languages: Collection<Language>? = null,
    val translateLanguages: Collection<Language>? = null,
    val categories: Collection<String>? = null,

    val cefrs: Collection<CEFR>? = null,
    val types: Collection<WordType>? = null,


    val translate : String? = null,
    val original : String? = null,


    val sortField: UserWordSortBy = UserWordSortBy.CREATED_AT,
    val asc: Boolean = false,
    val page: Int = 0,
    val size: Int = 20,
) : Queryable
