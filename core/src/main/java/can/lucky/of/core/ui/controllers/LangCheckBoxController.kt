package can.lucky.of.core.ui.controllers

import android.view.ViewGroup
import android.widget.TextView
import can.lucky.of.core.R
import can.lucky.of.core.domain.models.enums.Language

class LangCheckBoxController(
    private val startValue: Language,
    private val viewGroup: ViewGroup,
    private val width: Float,
    private val onClick : (lang: Language) -> Unit
) {
    private val mapLangText: MutableMap<Language, TextView> = mutableMapOf()
    private var langNow : Language = startValue

    init {
        Language.entries.forEach {
            if (it == Language.UNDEFINED){
                return@forEach
            }
            val checkBox = TextView(viewGroup.context)
            checkBox.width = width.toInt()
            checkBox.setTextColor(viewGroup.context.getColor(R.color.primary_green_dark))
            checkBox.textSize = 22f
            checkBox.textAlignment = TextView.TEXT_ALIGNMENT_CENTER

            mapLangText[it] = checkBox
            checkBox.text = it.titleCase
            checkBox.setOnClickListener { _ ->
                handleClick(it)
            }
            viewGroup.addView(checkBox)

            if (it == startValue){
                checkBox.setTextColor(viewGroup.context.getColor(R.color.primary_green))
            }
        }
    }

    private fun handleClick(lang: Language) {
        mapLangText[langNow]?.setTextColor(viewGroup.context.getColor(R.color.primary_green_dark))

        langNow = if (langNow != lang){
            mapLangText[lang]?.setTextColor(viewGroup.context.getColor(R.color.primary_green))
            lang
        }else{
            Language.UNDEFINED
        }

        onClick(langNow)
    }

    fun clear(){
        langNow = Language.UNDEFINED
        mapLangText.values.forEach {
            it.setTextColor(viewGroup.context.getColor(R.color.primary_green_dark))
        }
        onClick(langNow)
    }
}