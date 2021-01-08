package com.telehealthmanager.app.ui.timeslot

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ItemTimeBinding

class TimeSlotTimeAdapter(
    val context: Context,
    val items: List<SloItem>,
    val listener: IListener, private val isCurrentDate: Boolean
) : RecyclerView.Adapter<TimeSlotTimeViewHolder>(), ITimeListener {

    override fun onBindViewHolder(holder: TimeSlotTimeViewHolder, position: Int) {
        val item = items[position]
        holder.itemBinding.tvSession.text = item.session

        val timeSlotDayAdapter = items[position].sessionTimeList?.let {
            TimeSlotTimeInsideAdapter(context, it, position, this, isCurrentDate)
        }
        holder.itemBinding.rvTime.adapter = timeSlotDayAdapter
        holder.itemBinding.rvTime.layoutManager = GridLayoutManager(context, 5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotTimeViewHolder {

        val inflate = DataBindingUtil.inflate<ItemTimeBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_time, parent, false
        )
        return TimeSlotTimeViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemClick(position: Int, position1: Int, selectionItem: SlotTimeItem) {
        listener.onItemClick(position, position1, selectionItem)
    }
}

interface IListener {
    fun onItemClick(
        position: Int, position2: Int,
        selectionItem: SlotTimeItem
    )
}

class TimeSlotTimeViewHolder(view: ItemTimeBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}