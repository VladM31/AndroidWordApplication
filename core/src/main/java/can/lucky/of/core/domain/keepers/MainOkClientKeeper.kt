package can.lucky.of.core.domain.keepers

import okhttp3.OkHttpClient

data class MainOkClientKeeper(
    val okHttpClient: OkHttpClient
)