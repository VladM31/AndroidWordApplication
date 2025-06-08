package can.lucky.of.addword.ui.listeners

import android.view.View
import android.widget.AdapterView

class ItemSelectedListener<T>(
    private val onItemSelected: (T) -> Unit

) : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        onItemSelected(parent?.getItemAtPosition(position) as T)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}