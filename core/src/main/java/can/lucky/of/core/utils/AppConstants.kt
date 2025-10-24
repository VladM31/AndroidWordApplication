package can.lucky.of.core.utils

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig

object AppConstants {

    val telegramBotLink: String by lazy {
        Firebase.remoteConfig.getString("telegram_bot_link")
    }


    fun baseUrl(): String {
        return Firebase.remoteConfig.getString("base_url")
    }
}