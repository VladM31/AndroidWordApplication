package com.generagames.happy.town.farm.wordandroid.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request

inline fun <reified T> OkHttpClient.fetch(req: Request, defValue: T, jsonMapper: Gson, tag: String) : T{
    val response = newCall(req).execute()

    if (!response.isSuccessful){
        Log.d(tag, "Error: ${response.message}")
        return defValue
    }

    val body = response.body?.string()

    if (body == null){
        Log.d(tag, "Error: body is null")
        return defValue
    }

    val type = object : TypeToken<T>() {}.type
    return jsonMapper.fromJson(body, type)
}