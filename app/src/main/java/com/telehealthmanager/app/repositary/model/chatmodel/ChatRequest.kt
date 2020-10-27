package com.telehealthmanager.app.repositary.model.chatmodel


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChatRequest(
    @SerializedName("chennel")
    val chennel: String,
    @SerializedName("finished_at")
    val finishedAt: String,
    @SerializedName("hospital_id")
    val hospitalId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("messages")
    val messages: String,
    @SerializedName("paid")
    val paid: Int,
    @SerializedName("paid_hours")
    val paidHours: Int,
    @SerializedName("patient_id")
    val patientId: Int,
    @SerializedName("payment_mode")
    val paymentMode: String,
    @SerializedName("speciality_id")
    val specialityId: Int,
    @SerializedName("started_at")
    val startedAt: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("use_wallet")
    val useWallet: Int
):Serializable