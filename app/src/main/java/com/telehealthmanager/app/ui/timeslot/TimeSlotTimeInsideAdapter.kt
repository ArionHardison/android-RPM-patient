package com.telehealthmanager.app.ui.timeslot

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ItemDatBinding
import com.telehealthmanager.app.utils.ViewUtils
import java.util.*

class TimeSlotTimeInsideAdapter(
    val context: Context,
    val items: List<SlotTimeItem>,
    var position1: Int,
    val listener: ITimeListener,
    private val isCurrentTime: Boolean
) : RecyclerView.Adapter<TimeSlotTimeDAyViewHolder>() {

    override fun onBindViewHolder(holder: TimeSlotTimeDAyViewHolder, position: Int) {
        val item = items[position]
        holder.itemBinding.tvDay.text = ViewUtils.getRailWaytoNormal(item.time).split(" ".toRegex()).toTypedArray().get(0)
        holder.itemBinding.tvDate.text = ViewUtils.getRailWaytoNormal(item.time).split(" ".toRegex()).toTypedArray().get(1)

        val date1 = item.time
        val date2 = ViewUtils.getTime(Date())
        if (item.isSelected) {
            holder.itemBinding.consDay.setBackgroundResource(R.drawable.circle_grey)
            holder.itemBinding.tvDay.setTextColor(Color.parseColor("#FFFFFF"))
            holder.itemBinding.tvDate.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemBinding.consDay.setBackgroundResource(R.drawable.circle_percentage)
            holder.itemBinding.tvDay.setTextColor(Color.parseColor("#2891fb"))
            holder.itemBinding.tvDate.setTextColor(Color.parseColor("#2891fb"))
        }

        val time = date2.compareTo(date1) < 0
        if (isCurrentTime) {
            if (!time) {
                holder.itemView.isEnabled = false
                holder.itemBinding.consDay.setBackgroundResource(R.drawable.circle_disable)
                holder.itemBinding.tvDay.setTextColor(Color.parseColor("#000000"))
                holder.itemBinding.tvDate.setTextColor(Color.parseColor("#000000"))
            }
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(position1, position, items.get(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotTimeDAyViewHolder {
        val inflate = DataBindingUtil.inflate<ItemDatBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_dat, parent, false
        )
        return TimeSlotTimeDAyViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

interface ITimeListener {
    fun onItemClick(position: Int, position1: Int, selectionItem: SlotTimeItem)
}

class TimeSlotTimeDAyViewHolder(view: ItemDatBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}