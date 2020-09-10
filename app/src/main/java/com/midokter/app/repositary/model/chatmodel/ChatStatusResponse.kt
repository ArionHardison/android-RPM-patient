package com.midokter.app.repositary.model.chatmodel


import com.google.gson.annotations.SerializedName

data class ChatStatusResponse(
    @SerializedName("check_status")
    val checkStatus: CheckStatus,
    @SerializedName("status")
    val status: String
)