package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.databinding.FavDoctorListItemBinding
import com.midokter.app.databinding.ListItemRelativeMangBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.midokter.app.utils.ViewUtils
import java.io.Serializable

class RelativemanagerAdapter(val items: MutableList<MainResponse.Doctor>, val context: Context) :
    RecyclerView.Adapter<RelativemanagerViewHolder>() {

    override fun onBindViewHolder(holder: RelativemanagerViewHolder, position: Int) {
        val item=items!![position]
        holder.itemBinding.name?.text = item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
        holder.itemBinding.age?.text = item.hospital?.doctor_profile?.speciality?.name
        ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView8, BuildConfig.BASE_IMAGE_URL.plus(item.hospital?.doctor_profile?.profile_pic))
        holder.itemView.setOnClickListener {
            val intent = Intent(context, SearchDoctorDetailActivity::class.java)
            intent.putExtra(WebApiConstants.IntentPass.FavDoctorProfile,item as Serializable)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelativemanagerViewHolder {

        val inflate = DataBindingUtil.inflate<ListItemRelativeMangBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_relative_mang, parent, false)
        return RelativemanagerViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class RelativemanagerViewHolder(view: ListItemRelativeMangBinding) : RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}