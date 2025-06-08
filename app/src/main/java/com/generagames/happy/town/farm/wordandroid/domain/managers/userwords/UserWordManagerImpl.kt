package com.generagames.happy.town.farm.wordandroid.domain.managers.userwords

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.userwords.UserWordManager
import can.lucky.of.core.domain.models.data.words.DeleteUserWord
import can.lucky.of.core.domain.models.data.words.GradeUserWord
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.domain.models.filters.UserWordFilter
import can.lucky.of.core.utils.toPair
import com.generagames.happy.town.farm.wordandroid.domain.utils.LanguageSerializer
import com.generagames.happy.town.farm.wordandroid.net.clients.userword.UserWordClient
import com.generagames.happy.town.farm.wordandroid.net.models.requests.DeleteUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.GradeUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.WordRespond
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class UserWordManagerImpl(
    private val userCacheManager: UserCacheManager,
    private val userWordClient: UserWordClient
) : UserWordManager {
    private val gson = GsonBuilder().registerTypeAdapter(Language::class.java, LanguageSerializer()).create()

    override suspend fun countBy(filter: UserWordFilter): Long {
        return userWordClient.countBy(
            userCacheManager.toPair().second,
            filter.copy(ids = null, wordIds = null).toQueryMap(gson),
            filter.wordIds,
            filter.ids
        ).count
    }

    override suspend fun findBy(filter: UserWordFilter): List<UserWord> {
        return userWordClient.findBy(
            userCacheManager.toPair().second,
            filter.copy(ids = null, wordIds = null).toQueryMap(gson),
            filter.wordIds,
            filter.ids
        ).map {
            UserWord(
                id = it.id,
                learningGrade = it.learningGrade,
                dateOfAdded = it.dateOfAdded,
                lastReadDate = it.lastReadDate,
                word = it.word.toWord()
            )
        }
    }

    override suspend fun putGrades(requests: List<GradeUserWord>): IntArray {
        return userWordClient.putGrades(
            userCacheManager.toPair().second,
            requests.map {
                GradeUserWordRequest(
                    userWordId = it.userWordId,
                    value = it.value
                )
            }
        )
    }

    override suspend fun delete(requests: List<DeleteUserWord>): IntArray {
        return userWordClient.delete(
            userCacheManager.toPair().second,
            requests.map {
                DeleteUserWordRequest(
                    id = it.id,
                    wordId = it.wordId
                )
            }
        )
    }

    companion object {
        private fun WordRespond.toWord(): Word {
            return Word(
                id = id,
                original = original,
                translate = translate,
                lang = lang,
                translateLang = translateLang,
                cefr = cefr,
                description = description,
                category = category,
                soundLink = soundLink,
                imageLink = imageLink
            )
        }
    }
}