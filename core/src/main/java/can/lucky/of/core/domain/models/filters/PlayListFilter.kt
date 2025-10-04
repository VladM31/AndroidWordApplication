package can.lucky.of.core.domain.models.filters

import can.lucky.of.core.domain.models.enums.PlaylistSortField

data class PlayListFilter(
    val ids: List<String>? = null,
    val userIds: List<String>? = null,
    val name: String? = null,
    val sortField: PlaylistSortField = PlaylistSortField.CREATED_AT,
    val asc: Boolean = false,
    val page: Int = 0,
    val size: Int = 20
): Queryable