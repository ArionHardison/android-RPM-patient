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

class VisitedDoctorsListAdapter(val items: MutableList<Appointment>, val context: Context, val listener: IAppointmentListener) :
    RecyclerView.Adapter<VisitedDoctorsViewHolder>() {

    override fun onBindViewHolder(holder: VisitedDoctorsViewHolder, position: Int) {
        val item: Appointment = items[position]
        holder.itemBinding.upcomingDate.text = ViewUtils.getDayFormat(item.scheduled_at!!)
        holder.itemBinding.upcomingTime.text = ViewUtils.getTimeFormat(item.scheduled_at)
        holder.itemBinding.textView28.text = item.status?.toLowerCase()!!.capitalize()
        if (item.hospital != null) {
            holder.itemBinding.upcomingDoctorName.text = item.hospital?.first_name.plus(" ").plus(item.hospital?.last_name)
            if (item.hospital.clinic != null) {
                val clinic = item.hospital.clinic
                if (clinic?.name != null && clinic?.address != null) {
                    holder.itemBinding.upcomingHospitalName.text = clinic.name.plus(",").plus(clinic.address)
                } else {
                    holder.itemBinding.upcomingHospitalName.text = "No Location"
                    if (clinic?.name != null) {
                        holder.itemBinding.upcomingHospitalName.text = clinic.name
                    }

                    if (clinic?.address != null) {
                        holder.itemBinding.upcomingHospitalName.text = clinic.address
                    }
                }
            }
        }

        if (item.status.equals("CANCELLED", true)) {
            holder.itemBinding.textView28.setTextColor(ContextCompat.getColor(context, R.color.colorRed))
            holder.itemBinding.textView28.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLiteRed))
        } else if (item.status.equals("CHECKEDOUT", true)) {
            holder.itemBinding.textView28.setTextColor(ContextCompat.getColor(context, R.color.colorGreen))
            holder.itemBinding.textView28.setBackgroundColor(ContextCompat.getColor(context, R.color.colorLiteGreen))
            holder.itemBinding.textView28.text = "Consulted"
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitedDoctorsViewHolder {
        val inflate: VisitedDoctorsListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.visited_doctors_list_item, parent, false
        )
        return VisitedDoctorsViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface IAppointmentListener {
        fun onItemClick(selectedItem: Appointment)
    }
}


class VisitedDoctorsViewHolder(view: VisitedDoctorsListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}