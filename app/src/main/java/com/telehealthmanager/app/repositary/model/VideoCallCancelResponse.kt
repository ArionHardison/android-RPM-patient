package com.telehealthmanager.app.repositary.model


import com.google.gson.annotations.SerializedName

data class VideoCallCancelResponse(
    @SerializedName("data")
    val `data`: Int,
    @SerializedName("message")
    val message: String
)