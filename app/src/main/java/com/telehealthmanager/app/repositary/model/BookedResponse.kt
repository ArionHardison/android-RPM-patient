package com.telehealthmanager.app.repositary.model

import java.io.Serializable


data class BookedResponse(
    val appointment: Appointment,
    val status: Boolean,
    val message: String
) : Serializable {
    data class Appointment(
        val appointment_type: String,
        val booking_for: String,
        val consult_time: Int,
        val created_at: String,
        val description: Any,
        val doctor_id: String,
        val id: Int,
        val service_id: Any,
        val patient_id: Int,
        val scheduled_at: String,
        val status: String,
        val updated_at: String,
        val invoice: Invoice
    ) : Serializable
}
