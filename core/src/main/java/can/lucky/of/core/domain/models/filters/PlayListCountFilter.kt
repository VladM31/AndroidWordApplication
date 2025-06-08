package can.lucky.of.core.domain.models.filters

import can.lucky.of.core.domain.models.enums.PlaylistSortField


data class  PlayListCountFilter(
    val ids: Collection<String>? = null,
    val userIds: List<String>? = null,
    val name: String? = null,
    val count: Range<Long>? = null,
    val pagination: PageFilter<PlaylistSortField>? = null,
) : Queryable
