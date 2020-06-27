package com.midokter.app.repositary.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReminderResponse(
    @SerializedName("reminder")
    val reminder: List<Reminder> = listOf(),
    @SerializedName("status")
    val status: Int = 0
) :Serializable{
    data class Reminder(
        @SerializedName("alarm")
        val alarm: Int = 0,
        @SerializedName("date")
        val date: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("notify_me")
        val notifyMe: Int = 0,
        @SerializedName("patient_id")
        val patientId: Int = 0,
        @SerializedName("time")
        val time: String = ""
    ):Serializable
}