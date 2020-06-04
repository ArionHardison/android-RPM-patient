package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.databinding.ListItemFinddoctorCategoriesBinding
import com.midokter.app.repositary.model.CategoryResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity


class CategoriesListAdapter(val items: MutableList<CategoryResponse.Category>, val context: Context) :
    RecyclerView.Adapter<CategoriesViewHolder>(), Filterable {


    private var SearchList: MutableList<CategoryResponse.Category>? = null

    init {
        this.SearchList = items
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        val item=SearchList!![position]
        holder.itemBinding.textView45?.text = item.name
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FindDoctorsListActivity::class.java)
            context.startActivity(intent);
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflate = DataBindingUtil.inflate<ListItemFinddoctorCategoriesBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_finddoctor_categories, parent, false)
        return CategoriesViewHolder(inflate)

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
                    val filteredList = ArrayList<CategoryResponse.Category>()
                    for (row in items) {
                        if (row.name!!.toLowerCase().contains(charString.toLowerCase())) {
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

                SearchList = results?.values as MutableList<CategoryResponse.Category>
                notifyDataSetChanged()
            }

        }
    }
}

class CategoriesViewHolder(view:ListItemFinddoctorCategoriesBinding ) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view


}