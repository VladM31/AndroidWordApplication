package com.generagames.happy.town.farm.wordandroid.domain.managers.subscribe

import android.content.Context
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.managers.subscribe.SubscribeCacheManager
import can.lucky.of.core.domain.models.data.Cache
import can.lucky.of.core.domain.models.data.Subscribe
import com.generagames.happy.town.farm.wordandroid.net.clients.subscribe.SubscribeClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class SharedPrefSubscribeCacheManager(
    private val subscribeClient: SubscribeClient,
    private val userCacheManager: UserCacheManager,
    context: Context
) : SubscribeCacheManager {
    private val sharedPreferences =
        context.getSharedPreferences("SharedPrefSubscribeCacheManager", Context.MODE_PRIVATE)

    override suspend fun fetch(): Subscribe? {
        return withContext(Dispatchers.IO){
            val sub = subscribeClient.fetch()

            if (sub != null) {
                saveCache(sub.expirationDate)
            }

            sub?.let {
                Subscribe(it.expirationDate)
            }
        }
    }

    override suspend fun cache(): Cache<Subscribe?> {
        val id = userCacheManager.user.id

        val cacheDate = sharedPreferences.getString(CACHE_DATE_KEY + id, null)?.let {
            LocalDateTime.parse(it)
        }

        val expirationDate = sharedPreferences.getString(EXPIRATION_DATE_KEY + id, null)?.let {
            LocalDateTime.parse(it)
        }

        if (cacheDate != null && LocalDateTime.now().minute - cacheDate.minute < SAVE_CACHE_TIME && expirationDate != null) {
            return Cache(
                Subscribe(
                    expirationDate
                ), cacheDate
            )
        }

        val sub = fetch()?.also {
            saveCache(it.expirationDate)
        }

        if (sub == null) {
            sharedPreferences.edit().clear().apply()
        }

        return Cache.ofNow(sub)
    }

    override suspend fun isActiveSubscribe(): Boolean {
        val subCache = cache()

        if (subCache.isEmpty && subCache.durations < WORK_CACHE_TIME) {
            return false;
        }

        if (subCache.isEmpty && subCache.durations > WORK_CACHE_TIME) {
            val sub = fetch()
            return sub?.expirationDate?.isAfter(LocalDateTime.now()) ?: false
        }

        if (subCache.value?.expirationDate?.isAfter(LocalDateTime.now()) == true) {
            return true;
        }

        if (subCache.durations > WORK_CACHE_TIME) {
            val sub = fetch()
            return sub?.expirationDate?.isAfter(LocalDateTime.now()) ?: false
        }

        return false;
    }

    override suspend fun update(expirationDate: LocalDateTime) {
        saveCache(expirationDate)
    }

    private fun saveCache(expirationDate: LocalDateTime) {
        val id = userCacheManager.user.id
        sharedPreferences.edit()
            .putString(EXPIRATION_DATE_KEY + id, expirationDate.toString())
            .putString(CACHE_DATE_KEY + id, LocalDateTime.now().toString())
            .apply()
    }

    companion object {
        private const val EXPIRATION_DATE_KEY = "EXPIRATION_DATE_KEY_"
        private const val CACHE_DATE_KEY = "CACHE_DATE_KEY_"
        private const val SAVE_CACHE_TIME = 30
        private const val WORK_CACHE_TIME = 5
    }
}