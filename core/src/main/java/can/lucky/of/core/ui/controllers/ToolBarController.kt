package can.lucky.of.core.ui.controllers

import can.lucky.of.core.R
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import can.lucky.of.core.databinding.IconMenuBinding
import can.lucky.of.core.databinding.ToolBarBinding
import can.lucky.of.core.ui.adapters.IconMenuAdapter
import can.lucky.of.core.ui.decorators.SpacesItemDecoration
import can.lucky.of.core.ui.models.ToolBarIconPopupButton
import can.lucky.of.core.ui.models.ToolBarPopupButton
import can.lucky.of.core.utils.dp
import com.google.android.material.internal.ViewUtils.dpToPx


class ToolBarController(
    private val navController : NavController?,
    private val binding: ToolBarBinding,
    private val title: String,
    private val buttonImage: Int? = null,
    private val buttonAction: View.OnClickListener? = null
) {
    private val isPortraitOrientation = binding.root.resources.configuration.orientation == 1

    constructor(
        binding: ToolBarBinding,
        title: String,
        fragment: Fragment,
        buttonImage: Int? = null,
        buttonAction: View.OnClickListener? = null
    ) : this(NavHostFragment.findNavController(fragment), binding, title, buttonImage, buttonAction)

    fun setDefaultSettings(){
        setTitle(title)
        binding.backButton.setOnClickListener{
            navController?.navigateUp()
        }
        buttonImage?.let {
            binding.additionalButton.setImageResource(it)
            binding.additionalButton.setOnClickListener(buttonAction)
        }
    }

    fun setTitle(title: String) {
        val maxLength = if (isPortraitOrientation) 17 else 34

        binding.title.contentDescription = title
        binding.title.setOnClickListener {
            if (title.length > maxLength) {
                Toast.makeText(binding.root.context, title, Toast.LENGTH_SHORT).show()
            }
        }

        binding.title.text = title
    }

    fun setNavigateUp(listener : View.OnClickListener?){
        binding.backButton.setOnClickListener(listener)
    }

    fun addContextMenu(buttonImage: Int, btns: Collection<ToolBarPopupButton>){
        if (btns.isEmpty()) return

       val popup = PopupMenu(ContextThemeWrapper(binding.root.context, R.style.CustomPopupMenu), binding.additionalButton)
        binding.additionalButton.setImageResource(buttonImage)

        btns.forEach {
            popup.menu.add(it.title).setOnMenuItemClickListener(it.menuItemClickListener)
        }

        binding.additionalButton.setOnClickListener {
            popup.show()
        }
    }

    fun addContextIconMenu(buttonImage: Int,btns: List<ToolBarIconPopupButton>) {
        val ctx = binding.root.context
        val iconMenuBinding = IconMenuBinding.inflate(LayoutInflater.from(ctx))

        val popup = PopupWindow(iconMenuBinding.root, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true).apply {
            isOutsideTouchable = true
            elevation = 8f
        }

        val rv = iconMenuBinding.menuRecycler
        rv.addItemDecoration(SpacesItemDecoration(14))
        rv.layoutManager = GridLayoutManager(ctx, 1) // кол-во колонок
        rv.adapter = IconMenuAdapter(btns) {
            it.onClickListener?.invoke()
            popup.dismiss()
        }

        binding.additionalButton.setImageResource(buttonImage)
        binding.additionalButton.setOnClickListener {anchor ->
            iconMenuBinding.root.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            val popupWidth = iconMenuBinding.root.measuredWidth
            val xOff = (anchor.width - popupWidth) / 2
            val yOff = 2.dp.toInt()
            popup.showAsDropDown(anchor, xOff, yOff)
        }
    }

    fun addContextMenu(buttonImage: Int,vararg btns: ToolBarPopupButton){
        addContextMenu(buttonImage, btns.toList())
    }
}