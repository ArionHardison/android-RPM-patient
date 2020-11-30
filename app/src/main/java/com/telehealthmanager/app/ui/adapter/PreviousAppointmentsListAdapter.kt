package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.VisitedDoctorsListItemBinding
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.utils.ViewUtils

class PreviousAppointmentsListAdapter(
    val items: MutableList<Appointment>,
    val context: Context,
    val listener: IAppointmentListener
) :
    RecyclerView.Adapter<PreviousAppointmentsViewHolder>() {

    override fun onBindViewHolder(holder: PreviousAppointmentsViewHolder, position: Int) {
        val item: Appointment = items[position]
        holder.itemBinding.upcomingDate.text = ViewUtils.getDayFormat(item.scheduled_at!!)
        holder.itemBinding.upcomingTime.text = ViewUtils.getTimeFormat(item.scheduled_at)
        holder.itemBinding.textView28.text = item.status?.toLowerCase()!!.capitalize()

        if (item.hospital != null) {
            holder.itemBinding.upcomingDoctorName.text = item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
            holder.itemBinding.upcomingHospitalName.text = item.hospital.clinic.name.plus(",").plus(item.hospital.clinic?.address)
        }

        if (item.status.equals("CANCELLED", true)) {
            holder.itemBinding.textView28.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            holder.itemBinding.textView28.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLiteRed))
        } else if (item.status.equals("CHECKEDOUT", true)) {
            holder.itemBinding.textView28.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
            holder.itemBinding.textView28.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLiteGreen))
            holder.itemBinding.textView28.text = "CONSULTED"
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousAppointmentsViewHolder {
        val inflate = DataBindingUtil.inflate<VisitedDoctorsListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.visited_doctors_list_item, parent, false
        )
        return PreviousAppointmentsViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface IAppointmentListener {
        fun onItemClick(selectedItem: Appointment)
    }
}


class PreviousAppointmentsViewHolder(view: VisitedDoctorsListItemBinding) :
    RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}