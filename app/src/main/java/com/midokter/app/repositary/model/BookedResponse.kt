package com.midokter.app.repositary.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class BookedResponse(
    val appointment: Appointment,
    val status: Boolean,
    val message: String
) {
    data class Appointment(
        val appointment_type: String,
        val booking_for: String,
        val consult_time: Int,
        val created_at: String,
        val description: Any,
        val doctor_id: String,
        val id: Int,
        val service_id:Any,
        val patient_id: Int,
        val scheduled_at: String,
        val status: String,
        val updated_at: String
    )
}
