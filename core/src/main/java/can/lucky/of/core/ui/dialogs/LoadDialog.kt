package can.lucky.of.core.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.DialogFragment
import can.lucky.of.core.R

fun loadDialog(context: Context) : AlertDialog{

    return AlertDialog.Builder(context, android.R.style.Theme_Material_NoActionBar_Fullscreen)

        .setView(R.layout.fragment_loading)
        .setCancelable(false)
        .create()
}

fun fragmentLoadDialog() : DialogFragment{
    val  dialog = DialogFragment(R.layout.fragment_loading)
    dialog.setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Material_NoActionBar_Fullscreen)
    dialog.isCancelable = false

    return DialogFragment()
}