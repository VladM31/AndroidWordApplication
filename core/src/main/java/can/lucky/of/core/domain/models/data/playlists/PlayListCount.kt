package can.lucky.of.core.domain.models.data.playlists

import java.time.OffsetDateTime

data class PlayListCount(
    val id: String,
    val name: String,
    val createdAt: OffsetDateTime,
    val count: Long
)
