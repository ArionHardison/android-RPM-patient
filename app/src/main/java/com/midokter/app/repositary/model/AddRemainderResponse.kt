package com.midokter.app.repositary.model


import com.google.gson.annotations.SerializedName

data class AddRemainderResponse(
    @SerializedName("reminder")
    val reminder: Reminder = Reminder(),
    @SerializedName("status")
    val status: Int = 0
) {
    data class Reminder(
        @SerializedName("alarm")
        val alarm: String = "",
        @SerializedName("date")
        val date: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("notify_me")
        val notifyMe: String = "",
        @SerializedName("patient_id")
        val patientId: Int = 0,
        @SerializedName("time")
        val time: String = ""
    )
}