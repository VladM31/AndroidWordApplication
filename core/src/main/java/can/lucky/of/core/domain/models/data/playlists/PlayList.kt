package can.lucky.of.core.domain.models.data.playlists

import can.lucky.of.core.domain.models.data.words.PinnedWord


data class PlayList(
    val id: String,
    val name: String,
    val dateOfCreated: String,
    val words: List<PinnedWord>
)
