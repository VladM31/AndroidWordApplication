package com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.impl

import can.lucky.of.addword.net.models.responses.SharePlayListRespond
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.utils.toPair
import com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.SharePlayListManager
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SharePlayList
import com.generagames.happy.town.farm.wordandroid.domain.models.data.SharePlayListOption
import com.generagames.happy.town.farm.wordandroid.domain.models.data.ShareResult
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.SharePlayListClient
import com.generagames.happy.town.farm.wordandroid.net.models.responses.ShareRespond
import java.util.Base64

class SharePlayListManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val sharePlayListClient: SharePlayListClient
) : SharePlayListManager {
    override suspend fun sharePlayList(sharePlayListOption: SharePlayListOption): ShareResult {
        return sharePlayListClient.sharePlayList(
            userCacheManager.toPair().second,
            playListId = sharePlayListOption.playListId,
            width = sharePlayListOption.width,
            height = sharePlayListOption.height
        ).let {
            ShareResult(
                it.toByteArray(),
                it.liveTimeInSeconds
            )
        }
    }

    private fun ShareRespond.toByteArray(): ByteArray {
        return Base64.getDecoder().decode(qrCode)
    }

    override suspend fun getSharedPlayList(sharedId: String): SharePlayList {
        return sharePlayListClient.getSharedPlayList(
            userCacheManager.toPair().second,
            sharedId
        ).toModel()
    }

    override suspend fun acceptSharedPlayList(sharedId: String) {
        sharePlayListClient.acceptShare(
            userCacheManager.toPair().second,
            sharedId
        )
    }

    private fun SharePlayListRespond.toModel() : SharePlayList{
        return SharePlayList(
            name = name,
            shareId = shareId,
            words = words.map { it.toModel() }
        )
    }

    private fun SharePlayListRespond.SharePlayListWordRespond.toModel(): SharePlayList.SharePlayListWord{
        return SharePlayList.SharePlayListWord(
            original = original,
            lang = lang,
            translate = translate,
            translateLang = translateLang
        )
    }
}