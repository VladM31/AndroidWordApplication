package can.lucky.of.core.domain.managers.playlist

import can.lucky.of.core.domain.models.data.playlists.SavePlayList
import can.lucky.of.core.domain.models.data.playlists.UpdatePlayList
import can.lucky.of.core.domain.models.data.playlists.PlayList
import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.domain.models.data.playlists.PlayListGrade
import can.lucky.of.core.domain.models.filters.PlayListCountFilter
import can.lucky.of.core.domain.models.filters.PlayListFilter
import can.lucky.of.core.domain.models.filters.DeletePlayListFilter


interface PlayListManager {

    suspend fun countBy(filter: PlayListCountFilter): List<PlayListCount>

    suspend fun findBy(filter: PlayListFilter): List<PlayList>

    suspend fun save(playLists: List<SavePlayList>): List<String?>

    suspend fun update(playLists: List<UpdatePlayList>): IntArray

    suspend fun updateGrades(grades: List<PlayListGrade>): IntArray

    suspend fun delete(filter: DeletePlayListFilter): Int

}