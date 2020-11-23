package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ListItemDoctorImageBinding
import com.telehealthmanager.app.repositary.model.DoctorListResponse


class DoctorImageAdapter(
    val items: MutableList<DoctorListResponse.specialities.DoctorProfile>,
    val itemImage: ArrayList<String>,
    val context: Context
) :
    RecyclerView.Adapter<DoctorImageViewHolder>() {

    override fun onBindViewHolder(holder: DoctorImageViewHolder, position: Int) {
        /*val item = items!![position]
        ViewUtils.setDocViewGlide(
            context,
            holder.itemBinding.imgItem,
            BuildConfig.BASE_IMAGE_URL.plus(item?.profile_pic ?: "")
        )*/
        val params = RelativeLayout.LayoutParams(holder.itemBinding.doctor1.layoutParams)
        if (position != 0) {
            params.setMargins(-20, 0, 0, 0)
            holder.itemBinding.doctor1.layoutParams = params
        } else {
            params.setMargins(0, 0, 0, 0)
            holder.itemBinding.doctor1.layoutParams = params
        }
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
        return itemImage.size/*if (items.size < 5) {
            items.size
        } else {
            5
        }*/
    }
}

class DoctorImageViewHolder(view: ListItemDoctorImageBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}