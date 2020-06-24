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
import com.midokter.app.databinding.ListItemSearchCategoryBinding
import com.midokter.app.databinding.ListItemSearchClinicBinding
import com.midokter.app.databinding.SearchDoctorListItemBinding
import com.midokter.app.databinding.VisitedDoctorsListItemBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.Hospital
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.repositary.model.SearchResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.midokter.app.utils.ViewUtils

import java.io.Serializable

class SearchClinicListAdapter(val items: MutableList<SearchResponse.Clinic>, val context: Context) :
    RecyclerView.Adapter<SearchClinicViewHolder>() {

    override fun onBindViewHolder(holder:SearchClinicViewHolder, position: Int) {

        val item=items!![position]

        holder.itemBinding.textView90?.text = item.name
        holder.itemBinding.textView92?.text = item.address
        //ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView23, BuildConfig.BASE_IMAGE_URL.plus( item.hospitals[0]?.clinic?.clinic_photo))
        holder.itemView.setOnClickListener {
           /* val intent = Intent(context, FindDoctorsListActivity::class.java)
            intent.putExtra(WebApiConstants.IntentPass.ID,item.id)
            context.startActivity(intent);*/
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchClinicViewHolder {

        val inflate = DataBindingUtil.inflate<ListItemSearchClinicBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_search_clinic, parent, false)
        return SearchClinicViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class SearchClinicViewHolder(view: ListItemSearchClinicBinding) : RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}