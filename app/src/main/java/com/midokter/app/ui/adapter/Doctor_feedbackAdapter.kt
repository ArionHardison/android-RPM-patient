package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.databinding.ListItemFinddoctorCategoriesBinding
import com.midokter.app.databinding.SearchDoctorDetailListItemBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.CategoryResponse
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.repositary.model.Hospital
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.utils.ViewUtils


class Doctor_feedbackAdapter(val items: MutableList<Hospital.Feedback>, val context: Context) :
    RecyclerView.Adapter<FeedbackViewHolder>() {


    private var SearchList: MutableList<Hospital.Feedback>? = null

    init {
        this.SearchList = items
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {

        val item=SearchList!![position]
        holder.itemBinding.textView121?.text = item.experiences
        holder.itemBinding.textView122?.text = ViewUtils.getDayFormat(item.created_at)
        holder.itemBinding.textView124?.text = item.visited_for
        holder.itemBinding.textView125?.text = item.comments
        if (item.experiences.equals("LIKE",true)){
            holder.itemBinding. imageView28.setImageResource(R.drawable.like_active)
        }else
            holder.itemBinding. imageView28.setImageResource(R.drawable.dislike_active)
       // ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView16, BuildConfig.BASE_IMAGE_URL.plus(item?.image!!))


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val inflate = DataBindingUtil.inflate<SearchDoctorDetailListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_doctor_detail_list_item, parent, false)
        return FeedbackViewHolder(inflate)

    }
    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return SearchList!!.size
    }


}

class FeedbackViewHolder(view:SearchDoctorDetailListItemBinding ) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view


}