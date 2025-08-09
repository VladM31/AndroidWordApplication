package can.lucky.of.core.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import can.lucky.of.core.R
import can.lucky.of.core.ui.models.ToolBarIconPopupButton
import can.lucky.of.core.ui.models.ToolBarPopupButton

class IconMenuAdapter(
    private val items: List<ToolBarIconPopupButton>,
    private val onClicked: (ToolBarIconPopupButton) -> Unit
) : RecyclerView.Adapter<IconMenuAdapter.VH>() {

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        val btn: ImageButton = v.findViewById(R.id.btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_icon_menu, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, pos: Int) {
        val item = items[pos]

        h.btn.setImageResource(item.iconRes)
        item.iconBack?.let { h.btn.setBackgroundResource(it) }
        h.btn.setOnClickListener { onClicked(item) }
    }

    override fun getItemCount() = items.size
}
