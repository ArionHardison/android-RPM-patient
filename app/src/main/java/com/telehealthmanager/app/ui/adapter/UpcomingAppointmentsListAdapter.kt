package com.telehealthmanager.app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.UpcomingListItemBinding
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.utils.ViewUtils

class UpcomingAppointmentsListAdapter(
    val listener: IAppointmentListener
) : RecyclerView.Adapter<UpcomingAppointmentsViewHolder>() {

    private val items: MutableList<Appointment> = mutableListOf()

    private var position: Int = 0

    override fun onBindViewHolder(holder: UpcomingAppointmentsViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        val item = items[position]
        holder.itemBinding.upcomingDate.text = ViewUtils.getDayFormat(item.scheduled_at!!)
        holder.itemBinding.upcomingTime.text = ViewUtils.getTimeFormat(item.scheduled_at)

        if (item.hospital != null) {
            holder.itemBinding.upcomingDoctorName.text = item.hospital?.first_name.plus(" ").plus(item.hospital?.last_name)
            if (item.hospital.clinic != null) {
                val clinic = item.hospital.clinic
                clinic.let {
                    if (it.name != null && it.address != null) {
                        holder.itemBinding.upcomingHospitalName.text = it.name.plus(",").plus(it.address)
                    } else {
                        holder.itemBinding.upcomingHospitalName.text = "No Location"
                        if (it.name != null) {
                            holder.itemBinding.upcomingHospitalName.text = clinic.name
                        }
                        if (it.address != null) {
                            holder.itemBinding.upcomingHospitalName.text = clinic.address
                        }
                    }
                }
            }
        }

        holder.itemView.setOnClickListener {
            setPosition(this@UpcomingAppointmentsListAdapter, position)
            listener.onItemClick(item)
        }

        holder.itemBinding.textView28.setOnClickListener {
            setPosition(this@UpcomingAppointmentsListAdapter, position)
            listener.onCancelClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingAppointmentsViewHolder {
        val inflate = DataBindingUtil.inflate<UpcomingListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.upcoming_list_item, parent, false
        )
        return UpcomingAppointmentsViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(list: MutableList<Appointment>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun removeItem() {
        items.removeAt(getPosition())
        notifyDataSetChanged()
    }

    private fun getPosition(): Int {
        return position
    }

    companion object {
        fun setPosition(adapter: UpcomingAppointmentsListAdapter, position: Int) {
            adapter.position = position
        }
    }
}

interface IAppointmentListener {
    fun onItemClick(selectedItem: Appointment)
    fun onCancelClick(selectedItem: Appointment)
}

class UpcomingAppointmentsViewHolder(view: UpcomingListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}