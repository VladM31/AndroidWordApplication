package can.lucky.of.core.domain.managers.playlist

import can.lucky.of.core.domain.models.PagedModels
import can.lucky.of.core.domain.models.data.playlists.PlayList
import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.domain.models.data.playlists.PlayListGrade
import can.lucky.of.core.domain.models.data.playlists.SavePlayList
import can.lucky.of.core.domain.models.data.playlists.UpdatePlayList
import can.lucky.of.core.domain.models.filters.DeletePlayListFilter
import can.lucky.of.core.domain.models.filters.PlayListCountFilter
import can.lucky.of.core.domain.models.filters.PlayListFilter


interface PlayListManager {

    suspend fun countBy(filter: PlayListCountFilter): PagedModels<PlayListCount>

    suspend fun findBy(filter: PlayListFilter): PagedModels<PlayList>

    suspend fun save(playLists: List<SavePlayList>): Result<*>

    suspend fun update(playLists: List<UpdatePlayList>)

    suspend fun updateGrades(grades: List<PlayListGrade>)

    suspend fun delete(filter: DeletePlayListFilter)

}