package can.lucky.of.core.ui.models

import android.view.MenuItem

data class ToolBarPopupButton(
    val title: String,
    val menuItemClickListener: MenuItem.OnMenuItemClickListener
)
