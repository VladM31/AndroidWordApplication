package com.generagames.happy.town.farm.wordandroid.net.clients.subscribe

import com.generagames.happy.town.farm.wordandroid.net.models.responses.SubscribeRespond

interface SubscribeClient {
    suspend fun fetch() : SubscribeRespond?
}