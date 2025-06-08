package com.generagames.happy.town.farm.wordandroid.domain.managers.playlist

import com.generagames.happy.town.farm.wordandroid.domain.models.data.SharePlayList
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SharePlayListOption
import com.generagames.happy.town.farm.wordandroid.domain.models.data.ShareResult
import com.generagames.happy.town.farm.wordandroid.net.models.responses.ShareRespond

interface SharePlayListManager {

    suspend fun sharePlayList(sharePlayListOption: SharePlayListOption): ShareResult

    suspend fun getSharedPlayList(sharedId: String): SharePlayList

    suspend fun acceptSharedPlayList(sharedId: String)
}