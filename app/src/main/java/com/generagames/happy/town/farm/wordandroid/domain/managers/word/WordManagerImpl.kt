package com.generagames.happy.town.farm.wordandroid.domain.managers.word

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.word.WordManager
import can.lucky.of.core.domain.models.PagedModels
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.filters.WordFilter
import can.lucky.of.core.utils.toPair
import com.generagames.happy.town.farm.wordandroid.net.clients.word.WordClient
import com.generagames.happy.town.farm.wordandroid.net.models.responses.WordRespond

class WordManagerImpl(
    private val wordClient: WordClient,
    private val userCacheManager: UserCacheManager
) : WordManager {
    private val gson = com.google.gson.GsonBuilder().create()
    override suspend fun findBy(filter: WordFilter): PagedModels<Word> {
        return try {
            val query = filter.toQueryMap(gson)
            wordClient.findBy(userCacheManager.toPair().second, query)
                .let { PagedModels.of(it, this::toWord) }
        } catch (e: Exception) {
            e.printStackTrace()
            PagedModels.empty()
        }
    }

    override suspend fun findOne(filter: WordFilter): Word? {
        val newFilter = filter.copy(size = 1)
        return findBy(newFilter).content.firstOrNull()
    }

    private fun toWord(res: WordRespond): Word {
        return Word(
            id = res.id,
            original = res.original,
            translate = res.translate,
            lang = res.lang,
            translateLang = res.translateLang,
            cefr = res.cefr,
            description = res.description,
            category = res.category,
            soundLink = res.soundLink,
            imageLink = res.imageLink
        )
    }
}