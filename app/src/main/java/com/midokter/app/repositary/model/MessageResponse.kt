package com.midokter.app.repositary.model


import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)