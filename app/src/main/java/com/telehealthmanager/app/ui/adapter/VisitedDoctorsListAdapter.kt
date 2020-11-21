package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.VisitedDoctorsListItemBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Appointment
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class VisitedDoctorsListAdapter(val items: MutableList<Appointment>, val context: Context, val listener: IAppointmentListener) :
    RecyclerView.Adapter<VisitedDoctorsViewHolder>() {

    override fun onBindViewHolder(holder: VisitedDoctorsViewHolder, position: Int) {
        val item: Appointment = items[position]
        holder.itemBinding.upcomingDate.text = ViewUtils.getDayFormat(item.scheduled_at!!)
        holder.itemBinding.upcomingTime.text = ViewUtils.getTimeFormat(item.scheduled_at)
        holder.itemBinding.textView28.text = item.status?.toLowerCase()!!.capitalize()

        if (item.hospital != null) {
            holder.itemBinding.upcomingDoctorName.text = item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
            holder.itemBinding.upcomingHospitalName.text = item.hospital.clinic.name.plus(",").plus(item.hospital.clinic.address)
        }

        if (item.status.equals("CANCELLED", true)) {
            holder.itemBinding.textView28.setTextColor(context.resources.getColor(R.color.colorRed))
            holder.itemBinding.textView28.setBackgroundColor(context.resources.getColor(R.color.colorLiteRed))
        }

        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitedDoctorsViewHolder {
        val inflate = DataBindingUtil.inflate<VisitedDoctorsListItemBinding>(
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