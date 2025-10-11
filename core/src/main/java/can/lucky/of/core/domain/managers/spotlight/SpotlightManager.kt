package can.lucky.of.core.domain.managers.spotlight

interface SpotlightManager {

    fun needShow(fragmentTag: String): Boolean

    fun setShowed(fragmentTag: String)
}