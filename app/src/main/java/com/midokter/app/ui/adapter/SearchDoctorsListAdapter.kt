package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.databinding.SearchDoctorListItemBinding
import com.midokter.app.databinding.VisitedDoctorsListItemBinding
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.list_item_finddoctor_categories.view.*

class SearchDoctorsListAdapter(val items: MutableList<MainResponse.SearchDoctor>, val context: Context) :
    RecyclerView.Adapter<SearchDoctorsViewHolder>() {

    override fun onBindViewHolder(holder: SearchDoctorsViewHolder, position: Int) {

        val item=items!![position]
        holder.itemBinding.textView90?.text = item.first_name.plus(" ").plus(item.last_name)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SearchDoctorDetailActivity::class.java)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDoctorsViewHolder {

        val inflate = DataBindingUtil.inflate<SearchDoctorListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_doctor_list_item, parent, false)
        return SearchDoctorsViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class SearchDoctorsViewHolder(view: SearchDoctorListItemBinding) : RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}