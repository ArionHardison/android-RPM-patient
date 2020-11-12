package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.model.HomeResponse
import com.telehealthmanager.app.ui.activity.chat.ChatActivity
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorCategoriesActivity
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchDoctorActivity
import com.telehealthmanager.app.ui.activity.visitedDoctor.VisitedDoctorsActivity
import kotlinx.android.synthetic.main.home_list_item.view.*

class MenuAdapter(val items: ArrayList<HomeResponse.Menu>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.home_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: HomeResponse.Menu = items.get(position)
        holder.tvMenuName?.text = data.name
        holder.tvsubMenuName?.text = data.subname
        holder.imgMenu?.setImageResource(data.imgresouce)
        holder.itemView.setOnClickListener {
            if (holder.tvMenuName.text.toString().equals(context.resources.getString(R.string.find_doctors))) {
                val intent = Intent(context, FindDoctorCategoriesActivity::class.java)
                context.startActivity(intent);
            }
            if (holder.tvMenuName.text.toString().equals(context.resources.getString(R.string.chat))) {
                val intent = Intent(context, ChatActivity::class.java)
                context.startActivity(intent);
            }
            if (holder.tvMenuName.text.toString().equals(context.resources.getString(R.string.search_doctor))) {
                val intent = Intent(context, SearchDoctorActivity::class.java)
                context.startActivity(intent);
            }
            if (holder.tvMenuName.text.toString().equals(context.resources.getString(R.string.visted_doctor))) {
                val intent = Intent(context, VisitedDoctorsActivity::class.java)
                context.startActivity(intent);
            }
        }
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvMenuName = view.menuName
    val imgMenu = view.imageView8
    val tvsubMenuName = view.textView18
}