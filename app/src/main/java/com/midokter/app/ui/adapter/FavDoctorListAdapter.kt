package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.databinding.FavDoctorListItemBinding
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.utils.ViewUtils

class FavDoctorListAdapter(val items: MutableList<MainResponse.Doctor>, val context: Context) :
    RecyclerView.Adapter<FavDoctorViewHolder>() {

    override fun onBindViewHolder(holder: FavDoctorViewHolder, position: Int) {
        val item=items!![position]
        holder.itemBinding.favdoctorName?.text = item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
        holder.itemBinding.favDrTypeTxt?.text = item.hospital?.specialities_name
        ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView8, BuildConfig.BASE_IMAGE_URL.plus(item.hospital?.doctor_profile?.profile_pic))
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