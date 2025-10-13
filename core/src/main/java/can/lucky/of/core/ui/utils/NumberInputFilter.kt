package can.lucky.of.core.ui.utils

import android.text.InputFilter
import android.text.Spanned

class NumberInputFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        if (source == null) {
            return null
        }
        for (i in start until end) {
            if (Character.isDigit(source[i]).not()) {
                return ""
            }
        }
        return null
    }
}