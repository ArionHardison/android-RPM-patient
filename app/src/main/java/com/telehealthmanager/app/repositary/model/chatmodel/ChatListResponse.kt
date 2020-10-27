package com.telehealthmanager.app.repositary.model.chatmodel


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChatListResponse(
    @SerializedName("chats")
    val chats: List<Chat>
): Serializable