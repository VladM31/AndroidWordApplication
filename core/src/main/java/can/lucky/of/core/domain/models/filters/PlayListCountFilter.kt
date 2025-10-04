package can.lucky.of.core.domain.models.filters

import can.lucky.of.core.domain.models.enums.PlaylistSortField


data class  PlayListCountFilter(
    val ids: Set<String>? = null,
    val userIds: Set<String>? = null,
    val name: String? = null,
    val count: Range<Long>? = null,
    val sortField: PlaylistSortField = PlaylistSortField.CREATED_AT,
    val asc: Boolean = false,
    val page: Int = 0,
    val size: Int = 20
) : Queryable
