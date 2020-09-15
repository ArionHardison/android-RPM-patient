package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.databinding.OnlineAppointmentsListItemBinding
import com.midokter.app.repositary.model.chatmodel.Chat
import com.midokter.app.ui.activity.pubnub.PubnubChatActivity
import com.midokter.app.utils.ViewUtils
import java.io.Serializable


class ChatAdapter(val context: Context, val list: List<Chat>,val listener:IChatListener) :
    RecyclerView.Adapter<ChatViewHolder>() {

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = list.get(position);
        holder.listItemChat.patientName.text =
            String.format("%s %s", item.hospital?.first_name ?: "", item.hospital?.last_name?: "")
        holder.listItemChat.lastChatTxt.text = item.chatRequest?.messages?.toString() ?: ""
        if(item.chatRequest?.startedAt!=null)
            holder.listItemChat.textViewTime.text = ViewUtils.getTimeAgoFormat(item.chatRequest?.startedAt)
        if (item.hospital?.doctor_profile?.profile_pic!=null &&item.hospital.doctor_profile?.profile_pic.isNotEmpty())
            ViewUtils.setImageViewGlide(
                context,
                holder.listItemChat.imgProfilePic,
                item.hospital.doctor_profile?.profile_pic.toString()
            )

        holder.listItemChat.cardViewChatItem.setOnClickListener {
            listener.onChatClicked(item)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflate = DataBindingUtil.inflate<OnlineAppointmentsListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.online_appointments_list_item, parent, false
        )
        return ChatViewHolder(inflate)
    }


    override fun getItemCount(): Int {
        return list.size
    }
}

interface IChatListener{
    fun onChatClicked(item:Chat)
}

class ChatViewHolder(view: OnlineAppointmentsListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val listItemChat = view
}
