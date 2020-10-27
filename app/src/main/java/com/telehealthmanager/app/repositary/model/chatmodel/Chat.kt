package com.telehealthmanager.app.repositary.model.chatmodel


import com.google.gson.annotations.SerializedName
import com.telehealthmanager.app.repositary.model.Hospital
import java.io.Serializable

data class Chat(
    @SerializedName("chat_request")
    val chatRequest: ChatRequest,
    @SerializedName("chat_requests_id")
    val chatRequestsId: Int,
    @SerializedName("chennel")
    val chennel: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("hospital")
    val hospital: Hospital,
    @SerializedName("hospital_id")
    val hospitalId: Int,
    @SerializedName("id")
    val id: Int,
    @SerializedName("patient_id")
    val patientId: Int,
    @SerializedName("updated_at")
    val updatedAt: String
):Serializable