package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.databinding.FindDoctorListItemBinding
import com.midokter.app.repositary.model.CategoryResponse
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorBookingActivity
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.find_doctor_list_item.view.*
import kotlinx.android.synthetic.main.list_item_finddoctor_categories.view.*

class FindDoctorListAdapter(val items:  MutableList<DoctorListResponse.Doctor>, val context: Context,val listener:IDoctorListener)  :
    RecyclerView.Adapter<FindDoctorViewHolder>() , Filterable {
    private var SearchList: MutableList<DoctorListResponse.Doctor>? = null


    init {
        this.SearchList = items
    }


    override fun onBindViewHolder(holder: FindDoctorViewHolder, position: Int) {
        val item=SearchList!![position]
        holder.itemBinding.textView47?.text = item.first_name.plus(" ").plus(item.last_name)
//        holder.itemBinding.textView48?.text = item.specialities_name as String

        holder.itemBinding.button15.setOnClickListener(View.OnClickListener {
            listener.oncallclick(item.mobile)
        })
        holder.itemBinding.button16.setOnClickListener(View.OnClickListener {
            listener.onbookclick(item)
        })

        holder.itemView.setOnClickListener {

            listener.onitemclick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindDoctorViewHolder {

        val inflate = DataBindingUtil.inflate<FindDoctorListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.find_doctor_list_item, parent, false)
        return FindDoctorViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return SearchList!!.size
    }
    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    SearchList = items
                    notifyDataSetChanged()
                } else {
                    val filteredList = ArrayList<DoctorListResponse.Doctor>()
                    for (row in items) {
                        if (row.email!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    SearchList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = SearchList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                SearchList = results?.values as MutableList<DoctorListResponse.Doctor>
                notifyDataSetChanged()
            }

        }
    }
}
interface IDoctorListener{
    fun onbookclick(selectedItem:DoctorListResponse.Doctor)
    fun onitemclick(selectedItem:DoctorListResponse.Doctor)
    fun oncallclick(phone:String)
}
class FindDoctorViewHolder(view: FindDoctorListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}