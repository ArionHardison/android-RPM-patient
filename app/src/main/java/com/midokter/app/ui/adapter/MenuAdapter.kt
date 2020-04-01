package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.ui.activity.chat.ChatActivity
import com.midokter.app.ui.activity.findDoctors.FindDoctorCategoriesActivity
import com.midokter.app.ui.activity.visitedDoctor.VisitedDoctorsActivity
import kotlinx.android.synthetic.main.home_list_item.view.*

class MenuAdapter(val items: ArrayList<String>, val context: Context) :
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
        holder?.tvMenuName?.text = items.get(position)
        holder.itemView.setOnClickListener {
            if (holder.tvMenuName.text.toString().equals("Find Doctors")){
                val intent = Intent(context, FindDoctorCategoriesActivity::class.java)
                context.startActivity(intent);
            }
            if (holder.tvMenuName.text.toString().equals("Chat")){
                val intent = Intent(context, ChatActivity::class.java)
                context.startActivity(intent);
            }
            if (holder.tvMenuName.text.toString().equals("Visited Doctors")){
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
}