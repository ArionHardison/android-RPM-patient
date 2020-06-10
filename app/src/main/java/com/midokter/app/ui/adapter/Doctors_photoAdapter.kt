package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.databinding.ListItemPhotosBinding
import com.midokter.app.databinding.VisitedDoctorsListItemBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.list_item_finddoctor_categories.view.*
import kotlinx.android.synthetic.main.visited_doctors_list_item.view.*
import java.io.Serializable

class Doctors_photoAdapter(val items: MutableList<AppointmentResponse.Previous.Appointment>, val context: Context) :
    RecyclerView.Adapter<PhotosViewHolder>() {

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val item=items!![position]

             //ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView28, BuildConfig.BASE_IMAGE_URL.plus(item?.image!!))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflate = DataBindingUtil.inflate<ListItemPhotosBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_photos, parent, false)
        return PhotosViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class PhotosViewHolder(view: ListItemPhotosBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}