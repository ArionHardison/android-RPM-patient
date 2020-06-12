package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.databinding.ListItemAvailibilityBinding
import com.midokter.app.repositary.model.Hospital

class AvailabilityAdapter(val items: MutableList<Hospital.Timing>, val context: Context) :
    RecyclerView.Adapter<ServiceViewHolder>() {
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item=items[position]
        holder.itemBinding.tvTime.text = String.format("%s - %s - %s",item.day,item.start_time,item.end_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val inflate = DataBindingUtil.inflate<ListItemAvailibilityBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_availibility, parent, false)
        return ServiceViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class ServiceViewHolder(view: ListItemAvailibilityBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}