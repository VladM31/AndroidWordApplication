package com.generagames.happy.town.farm.wordandroid.utils

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type


class ToStringConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody?, String?>? {
        val strCon = Converter<ResponseBody?, String?> { value -> value?.string() ?: "" }


        return if (String::class.java == type) {
            strCon
        } else null
    }

    override fun requestBodyConverter(
        type: Type, parameterAnnotations: Array<Annotation?>?,
        methodAnnotations: Array<Annotation?>?, retrofit: Retrofit?
    ): Converter<String?, RequestBody?>? {
        return if (String::class.java == type) {
            Converter<String?, RequestBody?> { value -> RequestBody.create(MEDIA_TYPE, value ?: "") }
        } else null
    }

    companion object {
        private val MEDIA_TYPE: MediaType = "text/plain".toMediaType()
    }
}