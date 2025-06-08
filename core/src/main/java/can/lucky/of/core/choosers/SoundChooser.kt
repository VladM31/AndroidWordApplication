package can.lucky.of.core.choosers

import android.content.Intent
import androidx.fragment.app.Fragment

class SoundChooser(
    fragment: Fragment
) : AbstractChooser(fragment,"sound_temp_") {

    override fun start() {
        mutableChooserState.value =
            chooserState.value.copy(file = null, isSet = false, isSusses = false)

        val contentIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "audio/*"
        }

        Intent(Intent.ACTION_CHOOSER).run {
            putExtra(Intent.EXTRA_TITLE, "Sound chooser")
            putExtra(Intent.EXTRA_INTENT, contentIntent)
            worker.launch(this)
        }
    }
}