package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.FavDoctorListItemBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class FavDoctorListAdapter(val items: MutableList<MainResponse.Doctor>, val context: Context) :
    RecyclerView.Adapter<FavDoctorViewHolder>() {

    override fun onBindViewHolder(holder: FavDoctorViewHolder, position: Int) {
        val item=items!![position]
        holder.itemBinding.favdoctorName?.text = item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
        holder.itemBinding.favDrTypeTxt?.text = item.hospital?.doctor_profile?.speciality?.name
        ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView8, BuildConfig.BASE_IMAGE_URL.plus(item.hospital?.doctor_profile?.profile_pic))
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SearchDoctorDetailActivity::class.java)
            intent.putExtra(WebApiConstants.IntentPass.FavDoctorProfile,item as Serializable)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavDoctorViewHolder {

        val inflate = DataBindingUtil.inflate<FavDoctorListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.fav_doctor_list_item, parent, false)
        return FavDoctorViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class FavDoctorViewHolder(view: FavDoctorListItemBinding) : RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}