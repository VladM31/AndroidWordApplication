package can.lucky.of.core.utils.listeners

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class RequestFailedImageHideListener(
    private val image: ImageView
) : RequestListener<Drawable?> {
    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable?>,
        isFirstResource: Boolean
    ): Boolean {
        image.visibility = android.view.View.GONE
        return false
    }

    override fun onResourceReady(
        resource: Drawable,
        model: Any,
        target: Target<Drawable?>?,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        image.setImageDrawable(resource)
        return true
    }
}