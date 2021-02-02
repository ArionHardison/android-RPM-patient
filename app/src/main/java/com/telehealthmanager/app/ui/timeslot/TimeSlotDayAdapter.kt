package com.telehealthmanager.app.ui.timeslot

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ItemDatBinding
import java.util.*

class TimeSlotDayAdapter(
    val context: Context,
    val items: List<SlotSelectionItem>,
    val listener: IAppointmentListener
) : RecyclerView.Adapter<TimeSlotDayViewHolder>() {

    override fun onBindViewHolder(holder: TimeSlotDayViewHolder, position: Int) {
        val item = items[position]
        holder.itemBinding.tvDay.text = item.day.toUpperCase(Locale.ROOT)
        if (item.selectedItem) {
            holder.itemBinding.consDay.setBackgroundResource(R.drawable.circle_grey)
            holder.itemBinding.tvDay.setTextColor(Color.parseColor("#FFFFFF"))
            holder.itemBinding.tvDate.setTextColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemBinding.consDay.setBackgroundResource(R.drawable.circle_percentage)
            holder.itemBinding.tvDay.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorSecondary))
            holder.itemBinding.tvDate.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorSecondary))
        }

        holder.itemBinding.tvDate.text = item.date
        holder.itemBinding.tvDate.visibility = View.VISIBLE
        holder.itemView.setOnClickListener {
            listener.onItemClick(position, items.get(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotDayViewHolder {
        val inflate = DataBindingUtil.inflate<ItemDatBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_dat, parent, false
        )
        return TimeSlotDayViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

interface IAppointmentListener {
    fun onItemClick(
        position: Int,
        selectionItem: SlotSelectionItem
    )
}

class TimeSlotDayViewHolder(view: ItemDatBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}