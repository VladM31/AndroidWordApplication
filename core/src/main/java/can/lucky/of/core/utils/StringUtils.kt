package can.lucky.of.core.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private val mutHost = MutableStateFlow<String?>(null)
val host : StateFlow<String?> = mutHost

fun String.lengthNotBetween(start: Int, end: Int) : Boolean {
    return length.let { it < start || it > end }
}

fun Enum<*>.titleCase() : String {
    return name.titleCase()
}

fun String.titleCase() : String {
    if (isEmpty() or isBlank()){
        return this
    }

    return this[0].titlecaseChar() + substring(1).lowercase().replace("_", " ")
}

fun String.localhostUrlToEmulator() : String {
    return replace("localhost", host.value.orEmpty())
}

fun setHost(host: String){
    mutHost.value = host
}



