package can.lucky.of.core.ui.controllers

import android.view.ViewGroup
import android.widget.TextView
import can.lucky.of.core.R

class TypedCheckBoxController<T>(
    private val startValue: T?,
    values: List<T>,
    private val viewGroup: ViewGroup,
    private val width: Float,
    private val onClick : (T?) -> Unit
) {

    private val mapLangText: MutableMap<T, TextView> = mutableMapOf()
    private var langNow : T? = startValue

    init {
        values.forEach {

            val checkBox = TextView(viewGroup.context)
            checkBox.width = width.toInt()
            checkBox.setTextColor(viewGroup.context.getColor(R.color.primary_green_dark))
            checkBox.textSize = 22f
            checkBox.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

            mapLangText[it] = checkBox
            checkBox.text = it.toString()
            checkBox.setOnClickListener { _ ->
                handleClick(it)
            }
            viewGroup.addView(checkBox)

            if (it == startValue){
                checkBox.setTextColor(viewGroup.context.getColor(R.color.primary_green))
            }
        }
    }

    private fun handleClick(lang: T) {
        mapLangText[langNow]?.setTextColor(viewGroup.context.getColor(R.color.primary_green_dark))

        langNow = if (langNow != lang){
            mapLangText[lang]?.setTextColor(viewGroup.context.getColor(R.color.primary_green))
            lang
        }else{
            null
        }

        onClick(langNow)
    }

    fun clear(){
        langNow = null
        mapLangText.values.forEach {
            it.setTextColor(viewGroup.context.getColor(R.color.primary_green_dark))
        }
        onClick(langNow)
    }
}