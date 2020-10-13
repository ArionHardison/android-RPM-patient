package com.midokter.app.repositary.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class VideoStatusCheck(
    @SerializedName("data")
    val data: Data,
    @SerializedName("message")
    val message: String
):Serializable {
    data class Data(
        @SerializedName("hospital_id")
        val hospitalId: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("patient_id")
        val patientId: Int,
        @SerializedName("room_id")
        val roomId: String,
        @SerializedName("status")
        val status: Int
    ) : Serializable
}