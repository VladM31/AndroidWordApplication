package can.lucky.of.core.domain.models.data.playlists

import can.lucky.of.core.domain.models.data.words.PinnedWord
import java.time.OffsetDateTime


data class PlayList(
    val id: String,
    val name: String,
    val createdAt: OffsetDateTime,
    val words: List<PinnedWord>
)
