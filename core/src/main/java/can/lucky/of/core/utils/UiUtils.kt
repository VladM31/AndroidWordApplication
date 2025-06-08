package can.lucky.of.core.utils

import android.content.res.Resources
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Spinner
import androidx.core.view.iterator
import can.lucky.of.core.domain.models.enums.Language
import can.lucky.of.core.ui.controllers.TypedCheckBoxController
import can.lucky.of.core.ui.controllers.LangCheckBoxController
import com.bumptech.glide.Glide
import can.lucky.of.core.R as CoreR


val Int.dp: Float
    get() {
        return this * Resources.getSystem().displayMetrics.density
    }

fun createLangCheckBox(viewGroup: ViewGroup, width: Float, startValue: Language = Language.UNDEFINED, onClick : (lang: Language) -> Unit) : LangCheckBoxController{
    return LangCheckBoxController(startValue, viewGroup, width, onClick)
}

fun <T> createTypedCheckBox(viewGroup: ViewGroup, width: Float, startValue: T?, values: List<T>, onClick : (lang: T?) -> Unit) : TypedCheckBoxController<T> {
    return TypedCheckBoxController(startValue,values, viewGroup, width, onClick)
}

fun EditText.addDebounceAfterTextChangedListener(delay: Long,listener: (s: String ) -> Unit){
    this.addTextChangedListener(object : TextWatcher{
        private val handler = Handler(Looper.getMainLooper())

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable) {
            handler.removeCallbacksAndMessages(null)
            handler.postDelayed({
                listener.invoke(s.toString())
            }, delay)
        }
    })
}

fun ImageView.setImageByUri(imageUri: Uri){
    if(imageUri.scheme?.startsWith("file") == true){
        Glide.with(context)
            .load(imageUri)
            .into(this)
    }else{
        setImageURI(imageUri)
    }
}

fun <T> Spinner.setContent(context: Array<T>){
    val adapter: ArrayAdapter<T> = ArrayAdapter<T>(
        this.context,
        CoreR.layout.spinner_dropdown_item,
        context
    )

    adapter.setDropDownViewResource(CoreR.layout.spinner_dropdown_item)

    this.adapter = adapter
}

inline fun <reified T> Spinner.setContent(context: Collection<T>){
    setContent(context.toTypedArray())
}

fun <T> Spinner.onSelect(listener: (T?) -> Unit){
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener.invoke(parent?.getItemAtPosition(position)?.let { it as T })
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

fun SeekBar.setOnProgressChanged(listener: (progress: Int) -> Unit){
    this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            listener.invoke(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {}

        override fun onStopTrackingTouch(seekBar: SeekBar?) {}
    })
}

fun Int.formatSecondsToMinutes(): String {
    val minutes = this / 60
    val remainingSeconds = this % 60
    return String.format("%02d:%02d", minutes, remainingSeconds)
}

fun ViewGroup.toList(): List<View> {
    val list = mutableListOf<View>()
    for (view in this.iterator()) {
        list.add(view)
    }
    return list
}