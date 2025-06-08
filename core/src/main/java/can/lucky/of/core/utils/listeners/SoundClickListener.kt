package can.lucky.of.core.utils.listeners

import android.media.MediaPlayer
import android.view.View
import android.widget.ImageButton
import can.lucky.of.core.R as CoreR


class SoundClickListener(
    private val mediaPlayer: MediaPlayer,
    private val btn: ImageButton,

) : View.OnClickListener {

    init {
        btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepareAsync()

            return
        }

        mediaPlayer.start()
        btn.setImageResource(CoreR.drawable.sound_disable)
    }
}