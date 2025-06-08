package can.lucky.of.core.domain.models.filters

import can.lucky.of.core.domain.models.filters.Queryable

data class DeletePlayListFilter(
    val ids: List<String>
) : Queryable {
    constructor(id: String) : this(listOf(id))
}