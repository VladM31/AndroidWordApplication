package can.lucky.of.core.domain.managers.media

import java.io.Closeable

interface MediaManager : AutoCloseable, Closeable {

    fun load(url: String, playWhenReady: Boolean)

    val isPlaying: Boolean

    fun start()

    fun stop()


}