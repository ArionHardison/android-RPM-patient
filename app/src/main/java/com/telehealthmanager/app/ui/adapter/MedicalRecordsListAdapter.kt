package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital1
import com.telehealthmanager.app.repositary.model.Medical
import com.telehealthmanager.app.repositary.model.Speciality
import com.telehealthmanager.app.ui.activity.medicalrecorddetails.MedicalRecordDetailsActivity
import com.telehealthmanager.app.utils.ViewUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.medical_records_list_item.view.*
import java.io.Serializable

class MedicalRecordsListAdapter(val items: List<Medical>, val context: Context) :
    RecyclerView.Adapter<MedicalRecordsViewHolder>() {

    override fun onBindViewHolder(holder: MedicalRecordsViewHolder, position: Int) {
        val medicalRecord: Medical = items[position]
        val hospital: Hospital1 = medicalRecord.hospital

        if (hospital.doctor_profile != null) {
            ViewUtils.setDocViewGlide(context,holder.imDocPics,BuildConfig.BASE_IMAGE_URL + hospital.doctor_profile.profile_pic)
        }

        holder.tvMrDoctorName.text = hospital.first_name + " " + hospital.last_name
        if (items[position].hospital.doctor_profile.speciality != null) {
            val specialities: Speciality = items[position].hospital.doctor_profile.speciality
            holder.tvDoctorType.text = specialities.name
        }


        holder.itemView.setOnClickListener {
            val intent = Intent(context, MedicalRecordDetailsActivity::class.java)
            intent.putExtra(WebApiConstants.IntentPass.MEDICAL_RECORD, medicalRecord as Serializable)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalRecordsViewHolder {
        return MedicalRecordsViewHolder(LayoutInflater.from(context).inflate(R.layout.medical_records_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class MedicalRecordsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvMrDoctorName: TextView = view.mrDoctorName
    val tvDoctorType: TextView = view.docTypeTxt
    val imDocPics: CircleImageView = view.mrDocPics
}