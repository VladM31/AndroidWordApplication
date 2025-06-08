package com.generagames.happy.town.farm.wordandroid.net.clients.word

import can.lucky.of.core.domain.models.filters.WordFilter
import com.generagames.happy.town.farm.wordandroid.net.models.requests.PinUserWordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.requests.WordRequest
import com.generagames.happy.town.farm.wordandroid.net.models.responses.WordRespond

interface WordClient {
    suspend fun save(words: Collection<WordRequest>): IntArray

    suspend fun pin(words: Collection<PinUserWordRequest>): IntArray

    suspend fun findBy(filter: WordFilter): List<WordRespond>
}