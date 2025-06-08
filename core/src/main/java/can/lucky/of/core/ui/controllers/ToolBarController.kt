package can.lucky.of.core.ui.controllers

import can.lucky.of.core.R
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import can.lucky.of.core.databinding.ToolBarBinding
import can.lucky.of.core.ui.models.ToolBarPopupButton


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

        binding.title.text = title.let {
//            if (it.length > maxLength) {
//                it.substring(0, maxLength - 3) + "..."
//            } else {
//                it
//            }
            it
        }
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

    fun addContextMenu(buttonImage: Int,vararg btns: ToolBarPopupButton){
        addContextMenu(buttonImage, btns.toList())
    }
}