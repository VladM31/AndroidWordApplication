package com.generagames.happy.town.farm.wordandroid.utils

import can.lucky.of.core.utils.host
import can.lucky.of.core.utils.localhostUrlToEmulator
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders



fun String.localhostUrlToEmulatorGlide(headers: LazyHeaders) : GlideUrl {
    return GlideUrl(this.localhostUrlToEmulator(),headers)
}

fun baseUrl() : String {
    return "http://${host.value}:8000"
}