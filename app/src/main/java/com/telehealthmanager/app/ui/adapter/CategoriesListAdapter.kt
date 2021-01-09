package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ListItemFinddoctorCategoriesBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.telehealthmanager.app.utils.ViewUtils


class CategoriesListAdapter(val items: MutableList<CategoryResponse.Category>, val context: Context) :
    RecyclerView.Adapter<CategoriesViewHolder>(), Filterable {

    private var searchList: MutableList<CategoryResponse.Category>? = null

    init {
        this.searchList = items
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val item: CategoryResponse.Category = searchList!![position]
        holder.itemBinding.textView45.text = item.name
        if (item.image != null) {
            ViewUtils.setImageViewGlide(context, holder.itemBinding.imageView16, BuildConfig.BASE_IMAGE_URL.plus(item.image))
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FindDoctorsListActivity::class.java)
            intent.putExtra(WebApiConstants.IntentPass.ID, item.id)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflate = DataBindingUtil.inflate<ListItemFinddoctorCategoriesBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_finddoctor_categories, parent, false
        )
        return CategoriesViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return searchList!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    searchList = items
                    notifyDataSetChanged()
                } else {
                    val filteredList = ArrayList<CategoryResponse.Category>()
                    for (row in items) {
                        if (row.name!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    searchList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = searchList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                searchList = results?.values as MutableList<CategoryResponse.Category>
                notifyDataSetChanged()
            }

        }
    }
}

class CategoriesViewHolder(view: ListItemFinddoctorCategoriesBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}