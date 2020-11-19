package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.model.CardList
import kotlinx.android.synthetic.main.list_item_card_list.view.*

class CardListAdapter(val items: List<CardList>, val context: Context, val listener: ICardItemClick) :
    RecyclerView.Adapter<CardViewHolder>() {
    private var selectedPosition = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_card_list, parent, false))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item: CardList = items[position]
        holder.tvCardNumber.text = "XXXX-XXXX-XXXX-".plus(item.last_four)
        if (selectedPosition != "") {
            holder.tvCardNumber.isChecked = selectedPosition.toInt() == position
        } else {
            holder.tvCardNumber.isChecked = false
        }
        holder.tvCardNumber.setOnClickListener {
            val selectedItem: CardList = items[position]
            listener.onItemClickCard(selectedItem)
            selectedPosition = position.toString()
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size
}

interface ICardItemClick {
    fun onItemClickCard(item: CardList)
}

class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvCardNumber: RadioButton = view.tvCardNumber
    val imgCardBrand: ImageView = view.cardBrand
}
