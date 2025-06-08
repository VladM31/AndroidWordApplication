package can.lucky.of.core.domain.models.filters

import com.google.gson.JsonObject
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

private fun appendQuery(obj: JsonObject, stringBuilder : MutableList<String>, prefix: String = "") {
    for (element in obj.entrySet()) {
        if (element.value.isJsonNull){
            continue
        }

        if (element.value.isJsonPrimitive){
            stringBuilder.add(((prefix + element.key) to element.value.asString).encode())
            continue
        }

        if (element.value.isJsonArray){
            for (arrayElement in element.value.asJsonArray){
                if (arrayElement.isJsonPrimitive){
                    stringBuilder.add(((prefix + element.key) to arrayElement.asString).encode())
                }
            }
            continue
        }

        if (element.value.isJsonObject){
            appendQuery(element.value.asJsonObject, stringBuilder, prefix + element.key + ".")
        }
    }
}

private fun Pair<String,String>.encode() : String {
    return runCatching {
        URLEncoder.encode(this.first, StandardCharsets.UTF_8.name()) + "=" + URLEncoder.encode(this.second, StandardCharsets.UTF_8.name())
    }.getOrDefault("")
}


interface Queryable {

    fun toQuery(gson: com.google.gson.Gson): String{
        val stringBuilder = mutableListOf<String>()
        val jsonElement = gson.toJsonTree(this)

        if (jsonElement.isJsonObject){
           if (jsonElement.asJsonObject.entrySet().isEmpty()){
               return ""
           }
            appendQuery(
                jsonElement.asJsonObject,
                stringBuilder
            )
            return stringBuilder.joinToString("&")
        }

        return ""
    }

    fun toQueryMap(gson: com.google.gson.Gson): Map<String,String>{
        val stringBuilder = mutableListOf<String>()
        val jsonElement = gson.toJsonTree(this)

        if (jsonElement.isJsonObject){
            if (jsonElement.asJsonObject.entrySet().isEmpty()){
                return emptyMap()
            }
            appendQuery(
                jsonElement.asJsonObject,
                stringBuilder
            )
            return stringBuilder.map { it.split("=") }.associate { it[0] to it[1]}
        }

        return emptyMap()
    }
}