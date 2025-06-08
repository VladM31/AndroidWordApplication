package com.generagames.happy.town.farm.wordandroid.domain.managers.playlist

import can.lucky.of.core.domain.models.data.playlists.PinPlayList

interface PinPlayListManager {

    suspend fun pin(requests: List<PinPlayList> ): IntArray

    suspend fun unpin(requests: List<PinPlayList> ): IntArray
}

