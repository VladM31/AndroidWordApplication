package can.lucky.of.core.utils

import android.media.MediaPlayer
import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import can.lucky.of.core.utils.listeners.RequestFailedImageHideListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import can.lucky.of.core.R as CoreR


fun ImageView.setGlideUrl(url: GlideUrl){
    Glide.with(context)
        .load(url)
        .addListener(RequestFailedImageHideListener(this))
        .placeholder(CoreR.drawable.image_icon)
        .fallback(CoreR.drawable.image_icon)
        .error(CoreR.drawable.image_icon)
        .into(this)
}

fun setImage(imageLink: String?, image: ImageView?, headers: LazyHeaders) {

    if (imageLink == null) {
        image?.visibility = View.GONE
        return
    }

    val url = GlideUrl(imageLink.localhostUrlToEmulator(), headers)

    image?.setGlideUrl(url)
}

fun setSound(soundButton: View,soundLink: String?,headers: Map<String, String> ) {
    if (soundLink == null) {
        return
    }

    val mediaPlayer = MediaPlayer()

    mediaPlayer.setDataSource(
        soundButton.context,
        soundLink.localhostUrlToEmulator().toUri(),
        headers
    )
    mediaPlayer.prepareAsync()

    soundButton.setOnClickListener {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
            mediaPlayer.prepareAsync()
        } else {
            mediaPlayer.start()
        }
    }
}