package can.lucky.of.core.ui.utils

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import can.lucky.of.core.databinding.OverlaySpotBinding
import can.lucky.of.core.domain.managers.spotlight.SpotlightManager
import can.lucky.of.core.utils.dp
import com.takusemba.spotlight.Target
import com.takusemba.spotlight.effet.FlickerEffect
import com.takusemba.spotlight.shape.RoundedRectangle
import kotlin.reflect.KClass

enum class HorizontalAlign { LEFT, CENTER, RIGHT }
enum class VerticalAlign { TOP, CENTER, BOTTOM }

fun OverlaySpotBinding.setTextPosition(
    horizontal: HorizontalAlign,
    vertical: VerticalAlign
) {
    val params = textContainer.layoutParams as FrameLayout.LayoutParams

    // горизонталь
    params.gravity = when (horizontal) {
        HorizontalAlign.LEFT -> Gravity.START
        HorizontalAlign.CENTER -> Gravity.CENTER_HORIZONTAL
        HorizontalAlign.RIGHT -> Gravity.END
    }

    // вертикаль
    params.gravity = params.gravity or when (vertical) {
        VerticalAlign.TOP -> Gravity.TOP
        VerticalAlign.CENTER -> Gravity.CENTER_VERTICAL
        VerticalAlign.BOTTOM -> Gravity.BOTTOM
    }

    textContainer.layoutParams = params
}

fun Fragment.buildRectangleTarget(
    anchor: View,
    title: String,
    desc: String,
    horizontal: HorizontalAlign = HorizontalAlign.CENTER,
    vertical: VerticalAlign = VerticalAlign.CENTER,
    onClick: (() -> Unit),
): Target {
    val (cx, cy) = anchor.centerInWindow()

    val extraPadding = 7.dp

    val width = anchor.width + extraPadding * 2
    val height = anchor.height + extraPadding * 2

    val binding = OverlaySpotBinding.inflate(LayoutInflater.from(requireContext()))
    binding.setTextPosition(horizontal, vertical)
    binding.title.text = title
    binding.desc.text = desc

    binding.root.setOnClickListener {
        onClick.invoke()
    }

    return Target.Builder()
        .setAnchor(cx, cy)
        .setShape(
            RoundedRectangle(
                height = height,
                width = width,
                radius = 16f
            )
        )
        .setEffect(FlickerEffect(40f, Color.argb(120, 68, 242, 193)))
        .setOverlay(binding.root)
        .build()
}

private fun View.centerInWindow(): Pair<Float, Float> {
    val loc = IntArray(2)
    getLocationInWindow(loc)
    return loc[0] + width / 2f to loc[1] + height / 2f
}

// All how extend Fragment
fun SpotlightManager.notNeedShow(fragment: KClass<out Fragment>): Boolean {
    return needShow(
        fragment.simpleName ?: throw IllegalArgumentException("Fragment has no simple name")
    ).not()
}

fun SpotlightManager.setShowed(fragment: KClass<out Fragment>) {
    setShowed(fragment.simpleName ?: throw IllegalArgumentException("Fragment has no simple name"))
}