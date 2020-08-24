package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.databinding.ListItemDoctorImageBinding
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.utils.ViewUtils

class DoctorImageAdapter(
    val items: MutableList<DoctorListResponse.specialities.DoctorProfile>,
    val context: Context
) :
    RecyclerView.Adapter<DoctorImageViewHolder>() {

    override fun onBindViewHolder(holder: DoctorImageViewHolder, position: Int) {
        val item = items!![position]
        ViewUtils.setImageViewGlide(
            context,
            holder.itemBinding.imgItem,
            BuildConfig.BASE_IMAGE_URL.plus(item?.profile_pic?:"")
        )
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorImageViewHolder {
        val inflate = DataBindingUtil.inflate<ListItemDoctorImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_doctor_image, parent, false
        )
        return DoctorImageViewHolder(inflate)

    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return if (items!!.size<5) {
            items!!.size
        } else {
            5
        }

    }
}

class DoctorImageViewHolder(view: ListItemDoctorImageBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view


}