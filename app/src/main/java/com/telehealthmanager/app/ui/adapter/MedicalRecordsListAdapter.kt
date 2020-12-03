package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.MedicalRecord
import com.telehealthmanager.app.repositary.model.chatmodel.Chat
import com.telehealthmanager.app.ui.activity.addmedicalrecord.DoctorMedicalRecords
import com.telehealthmanager.app.utils.ViewUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.medical_records_list_item.view.*
import java.io.Serializable

class MedicalRecordsListAdapter(val items: List<MedicalRecord.Medical>, val context: Context,val iMedicalRecordClick: IMedicalRecordClick) :
    RecyclerView.Adapter<MedicalRecordsViewHolder>() {

    override fun onBindViewHolder(holder: MedicalRecordsViewHolder, position: Int) {
        val medicalRecord: MedicalRecord.Medical = items[position]
        holder.tvMrDoctorName.text = medicalRecord?.first_name + " " + medicalRecord?.last_name
        if (medicalRecord?.doctor_profile != null) {
            ViewUtils.setDocViewGlide(context, holder.imDocPics, BuildConfig.BASE_IMAGE_URL + medicalRecord.doctor_profile?.profile_pic)
            holder.tvDoctorType.text = medicalRecord.doctor_profile?.speciality.name
        }

        holder.itemView.setOnClickListener {
            iMedicalRecordClick.onItemClicked(medicalRecord)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalRecordsViewHolder {
        return MedicalRecordsViewHolder(LayoutInflater.from(context).inflate(R.layout.medical_records_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

interface IMedicalRecordClick{
    fun onItemClicked(item: MedicalRecord.Medical)
}

class MedicalRecordsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvMrDoctorName: TextView = view.mrDoctorName
    val tvDoctorType: TextView = view.docTypeTxt
    val imDocPics: CircleImageView = view.mrDocPics
}