package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import kotlinx.android.synthetic.main.chat_problem_list_item.view.*
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.list_item_finddoctor_categories.view.*

class ChatProblemAreasListAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<ChatProblemsViewHolder>() {

    override fun onBindViewHolder(holder: ChatProblemsViewHolder, position: Int) {
        holder?.tvProblemName?.text = items.get(position)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FindDoctorsListActivity::class.java)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatProblemsViewHolder {
        return ChatProblemsViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.chat_problem_list_item,
                parent,
                false
            )
        )
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class ChatProblemsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvProblemName = view.textView80
}