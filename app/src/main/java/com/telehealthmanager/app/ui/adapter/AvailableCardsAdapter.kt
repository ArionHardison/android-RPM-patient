package com.telehealthmanager.app.ui.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.model.CardList
import kotlinx.android.synthetic.main.list_item_card.view.*

class AvailableCardsAdapter(
    val items: List<CardList>,
    val context: Context,
    val listener: ICardClick
) :
    RecyclerView.Adapter<AvailableViewHolder>() {
    private var selectedPosition = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvailableViewHolder {
        return AvailableViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_card, parent, false))
    }

    override fun onBindViewHolder(holder: AvailableViewHolder, position: Int) {
        val item: CardList = items[position]
        holder.tvCardNumber.text = "XXXX-XXXX-XXXX-".plus(item.last_four)
        holder.tvCardNumber.setOnClickListener {
            val selectedItem: CardList = items[position]
            listener.onItemClickCard(selectedItem)
            selectedPosition = position.toString()
            notifyDataSetChanged()
        }

        holder.tvCardNumber.setOnLongClickListener {
            val selectedItem: CardList = items[position]
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setMessage(context.getString(R.string.delete_card_info))
                .setCancelable(false)
                .setPositiveButton(R.string.yes) { dialog, id ->
                    listener.onItemDelete(selectedItem)
                    dialog.dismiss()
                }.setNegativeButton(R.string.no) { dialog, id -> dialog.dismiss() }
            val alert: AlertDialog = builder.create()
            alert.show()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = items.size
}

interface ICardClick {
    fun onItemClickCard(item: CardList)
    fun onItemDelete(item: CardList)
}

class AvailableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvCardNumber: TextView = view.tvCardNumber
    val imgCardBrand: ImageView = view.cardBrand
}
