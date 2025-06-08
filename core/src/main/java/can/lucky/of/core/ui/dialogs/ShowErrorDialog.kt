package can.lucky.of.core.ui.dialogs

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

fun Context.showError(message: String) : AlertDialog{
    return AlertDialog.Builder(this, can.lucky.of.core.R.style.DialogStyle)
        .setMessage(message)
        .setTitle("Valid error")
        .setPositiveButton("Ok") { _, _ -> }
        .create()
}
