package com.generagames.happy.town.farm.wordandroid.domain.managers.cache

import android.content.Context
import can.lucky.of.core.domain.managers.cache.UserCacheManager
import can.lucky.of.core.domain.models.data.Token
import can.lucky.of.core.domain.models.data.User
import com.generagames.happy.town.farm.wordandroid.utils.ctyprs.TokenCipherImpl
import com.google.gson.Gson

class SharedUserCacheManager(
    context: Context
): UserCacheManager {
    private val sharedPreferences = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE)
    private val jsonMapper = Gson()
    private val tokenCipher = TokenCipherImpl()

    override val user: User
        get() = sharedPreferences.getString(USER_KEY, null)?.let {
            jsonMapper.fromJson(it, User::class.java)
        } ?: throw IllegalStateException("User not found")

    override val token: Token
        get() {
            val token = sharedPreferences.getString(TOKEN_KEY, null)?.let {
                jsonMapper.fromJson(it, Token::class.java)
            } ?: Token.DEFAULT

            val newValue = tokenCipher.decrypt(token.value)

            return token.copy(value = newValue)
        }

    override val isExpired: Boolean
        get() = sharedPreferences.getString(TOKEN_KEY, null)?.let {
            jsonMapper.fromJson(it, Token::class.java)
        }?.isExpired() ?: true

    override fun saveUser(user: User) {
        sharedPreferences.edit()
            .putString(USER_KEY, jsonMapper.toJson(user))
            .apply()
    }

    override fun saveToken(token: Token) {
        val newValue = tokenCipher.encrypt(token.value)

        sharedPreferences.edit()
            .putString(TOKEN_KEY, jsonMapper.toJson(token.copy(value = newValue)))
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit()
            .clear()
            .apply()
    }

    companion object{
        const val TOKEN_KEY = "TOKEN_KEY"
        const val USER_KEY = "USER_KEY"
        const val CACHE_NAME = "shared_user_cache_manager"
    }
}