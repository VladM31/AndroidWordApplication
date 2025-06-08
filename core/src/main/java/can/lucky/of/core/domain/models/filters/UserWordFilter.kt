package can.lucky.of.core.domain.models.filters

import can.lucky.of.core.domain.models.enums.CEFR
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.enums.UserWordSortBy


data class UserWordFilter(
    val originalLang : Language? = null,
    val translateLang : Language? = null,
    val translate : String? = null,
    val original : String? = null,
    val categories : List<String>? = null,
    val wordIds : List<String>? = null,
    val ids: List<String>? = null,
    val cefrs: Collection<CEFR>? = null,
    val pagination: PageFilter<UserWordSortBy>? = null
) : Queryable
