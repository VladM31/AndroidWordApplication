package can.lucky.of.core.domain.models.filters

import com.google.gson.annotations.SerializedName

data class PageFilter<T>(
    val page: Long? = null,
    val size: Long? = null,
    @SerializedName("sortField")
    val sort: T? = null,
    val asc: Boolean = false
)
