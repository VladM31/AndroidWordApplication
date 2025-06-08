package com.generagames.happy.town.farm.wordandroid.utils

import android.annotation.SuppressLint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("ConstantLocale")
private val DETE_FORMATTERS = listOf(
    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()),
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()),
    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()),
)

fun String.toLocalDateTime(): LocalDateTime {
    DETE_FORMATTERS.forEach{
        this.runCatching {
            return@toLocalDateTime LocalDateTime.parse(this, it)
        }
    }
    TODO("Not yet implemented format for date: $this")
}