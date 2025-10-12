package com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.impl

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.data.playlists.PinPlayList
import can.lucky.of.core.utils.toPair
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.PinPlayListManager
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.PinPlayListClient
import com.generagames.happy.town.farm.wordandroid.net.models.requests.PinPlayRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PinPlayListManagerImpl(
    private val pinPlayListClient: PinPlayListClient,
    private val userCacheManager: UserCacheManager
) : PinPlayListManager {
    override suspend fun pin(requests: List<PinPlayList>) {
        return withContext(Dispatchers.IO){
            try {
                pinPlayListClient.pin(userCacheManager.toPair().second,requests.map { it.toRequest() })
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override suspend fun unpin(requests: List<PinPlayList>) {
        return withContext(Dispatchers.IO){
            try {
                pinPlayListClient.unpin(userCacheManager.toPair().second,requests.map { it.toRequest() })
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun PinPlayList.toRequest() =
        PinPlayRequest(
            wordId = wordId,
            playListId = playListId
        )
}