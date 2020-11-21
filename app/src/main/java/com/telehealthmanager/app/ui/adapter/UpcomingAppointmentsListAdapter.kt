package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.UpcomingListItemBinding
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.utils.ViewUtils

class UpcomingAppointmentsListAdapter(
    val items: MutableList<Appointment>,
    val context: Context,
    val listener: IAppointmentListener
) :
    RecyclerView.Adapter<UpcomingAppointmentsViewHolder>() {

    override fun onBindViewHolder(holder: UpcomingAppointmentsViewHolder, position: Int) {
        val item = items[position]
        holder.itemBinding.upcomingDate.text = ViewUtils.getDayFormat(item.scheduled_at!!)
        holder.itemBinding.upcomingTime.text = ViewUtils.getTimeFormat(item.scheduled_at)

        if (item.hospital != null) {
            holder.itemBinding.upcomingDoctorName.text = item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
            holder.itemBinding.upcomingHospitalName.text = item.hospital.clinic.name.plus(",").plus(item.hospital.clinic?.address)
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
        holder.itemBinding.textView28.setOnClickListener {
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
}

interface IAppointmentListener {
    fun onItemClick(selectedItem: Appointment)
    fun onCancelClick(selectedItem: Appointment)
}

class UpcomingAppointmentsViewHolder(view: UpcomingListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}