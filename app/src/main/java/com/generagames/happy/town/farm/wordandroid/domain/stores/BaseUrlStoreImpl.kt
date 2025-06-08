package com.generagames.happy.town.farm.wordandroid.domain.stores

import can.lucky.of.core.domain.storages.BaseUrlStore
import com.generagames.happy.town.farm.wordandroid.utils.baseUrl

class BaseUrlStoreImpl : BaseUrlStore {
    override fun getBaseUrl() = baseUrl()
}