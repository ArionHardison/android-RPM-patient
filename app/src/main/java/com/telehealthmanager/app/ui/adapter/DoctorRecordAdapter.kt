package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.model.ResponseMedicalDetails
import com.telehealthmanager.app.utils.ViewUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.medical_records_list_item.view.*

class DoctorRecordAdapter(val items: List<ResponseMedicalDetails.RecordDetail>, val context: Context, val iDoctorRecordClick: IDoctorRecordClick) :
    RecyclerView.Adapter<DoctorRecordViewHolder>() {

    override fun onBindViewHolder(holder: DoctorRecordViewHolder, position: Int) {
        val medicalRecord: ResponseMedicalDetails.RecordDetail = items[position]

        if(medicalRecord.created_by=="patient"){
            holder.tvDoctorType.text = context.getString(R.string.created_by_patient)
        }else{
            holder.tvDoctorType.text = context.getString(R.string.created_by_doctor)
        }

        if(medicalRecord.patient!=null){
            if (medicalRecord?.patient?.profile != null) {
                ViewUtils.setDocViewGlide(context, holder.imDocPics, BuildConfig.BASE_IMAGE_URL + medicalRecord?.patient?.profile?.profile_pic)
            }
            holder.tvMrDoctorName.text = medicalRecord?.patient?.first_name + " " + medicalRecord?.patient?.last_name
        }

        holder.itemView.setOnClickListener {
            iDoctorRecordClick.onItemClicked(medicalRecord)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorRecordViewHolder {
        return DoctorRecordViewHolder(LayoutInflater.from(context).inflate(R.layout.medical_records_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

interface IDoctorRecordClick {
    fun onItemClicked(item: ResponseMedicalDetails.RecordDetail)
}

class DoctorRecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvMrDoctorName: TextView = view.mrDoctorName
    val tvDoctorType: TextView = view.docTypeTxt
    val imDocPics: CircleImageView = view.mrDocPics
}