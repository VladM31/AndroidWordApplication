package com.generagames.happy.town.farm.wordandroid.domain.managers.playlist.impl

import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.playlist.PlayListManager
import can.lucky.of.core.domain.models.data.playlists.PlayList
import can.lucky.of.core.domain.models.data.playlists.PlayListCount
import can.lucky.of.core.domain.models.data.playlists.PlayListGrade
import can.lucky.of.core.domain.models.data.words.PinnedWord
import can.lucky.of.core.domain.models.data.words.UserWord
import can.lucky.of.core.domain.models.data.words.Word
import can.lucky.of.core.domain.models.filters.DeletePlayListFilter
import can.lucky.of.core.domain.models.filters.PlayListCountFilter
import can.lucky.of.core.domain.models.filters.PlayListFilter
import can.lucky.of.core.utils.toPair
import can.lucky.of.core.domain.models.data.playlists.SavePlayList
import can.lucky.of.core.domain.models.data.playlists.UpdatePlayList
import com.generagames.happy.town.farm.wordandroid.net.clients.playlist.PlayListClient
import com.generagames.happy.town.farm.wordandroid.net.models.requests.PlayListGradeRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.SavePlayListRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.UpdatePlayListRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.PlayListRespond
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class PlayListManagerImpl(
    private val playListClient: PlayListClient,
    private val userCacheManager: UserCacheManager
) : PlayListManager {
    private val gson = Gson()

    override suspend fun countBy(filter: PlayListCountFilter): List<PlayListCount> {
        return withContext(Dispatchers.IO) {
            playListClient.countBy(userCacheManager.toPair().second, filter.toQueryMap(gson)).map {
                PlayListCount(
                    id = it.id,
                    name = it.name,
                    dateOfCreated = it.dateOfCreated,
                    count = it.count
                )
            }
        }
    }

    override suspend fun save(playLists: List<SavePlayList>): List<String?> {
        return withContext(Dispatchers.IO) {
            try {
                playListClient.save(userCacheManager.toPair().second, playLists.map {
                    SavePlayListRequest(
                        name = it.name
                    )
                })
            }catch (e: Exception) {
                e.printStackTrace()
                return@withContext emptyList()
            }
        }
    }

    override suspend fun update(playLists: List<UpdatePlayList>): IntArray {
        return withContext(Dispatchers.IO) {
            playListClient.update(userCacheManager.toPair().second, playLists.map {
                UpdatePlayListRequest(
                    id = it.id,
                    name = it.name
                )
            })
        }
    }

    override suspend fun updateGrades(grades: List<PlayListGrade>): IntArray {
        return withContext(Dispatchers.IO) {
            playListClient.updateGrades(userCacheManager.toPair().second, grades.map {
                PlayListGradeRequest(
                    playListGrade = it.playListGrade,
                    wordId = it.wordId,
                    wordGrade = it.wordGrade
                )
            })
        }
    }

    override suspend fun delete(filter: DeletePlayListFilter): Int {
        return withContext(Dispatchers.IO) {
            playListClient.delete(userCacheManager.toPair().second, filter.toQueryMap(gson))
        }
    }

    override suspend fun findBy(filter: PlayListFilter): List<PlayList> {
        return withContext(Dispatchers.IO) {
            playListClient.findBy(userCacheManager.toPair().second, filter.toQueryMap(gson)).map {
                it.toPlayList()
            }
        }
    }

    private fun PlayListRespond.WordRespond.toWord(): Word {
        return Word(
            id = id,
            original = original,
            lang = lang,
            translate = translate,
            translateLang = translateLang,
            cefr = cefr,
            description = description,
            category = category,
            soundLink = soundLink,
            imageLink = imageLink
        )
    }

    private fun PlayListRespond.UserWordRespond.toUserWord(): UserWord {
        return UserWord(
            id = id,
            learningGrade = learningGrade,
            dateOfAdded = dateOfAdded,
            lastReadDate = lastReadDate,
            word = word.toWord()
        )
    }

    private fun PlayListRespond.PinnedWordRespond.toPinnedWord(): PinnedWord {
        return PinnedWord(
            learningGrade = learningGrade,
            lastReadDate = lastReadDate,
            userWord = word.toUserWord()
        )
    }

    private fun PlayListRespond.toPlayList(): PlayList {
        return PlayList(
            id = id,
            name = name,
            dateOfCreated = dateOfCreated,
            words = words.map { it.toPinnedWord() }
        )
    }
}