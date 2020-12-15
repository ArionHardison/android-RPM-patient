package com.telehealthmanager.app.repositary.model


import java.io.Serializable

data class Appointment(
    val appointment_type: String?,
    val booking_for: String?,
    val checkedout_at: Any?,
    val checkin_at: Any?,
    val consult_time: String?,
    val created_at: String?,
    val deleted_at: Any?,
    val description: Any?,
    val doctor_id: Int?,
    val doctor_reminder: Int?,
    val engaged_at: Any?,
    val hospital: Hospital?,
    val id: Int?,
    val patient_id: Int?,
    val patient_prescriptions: List<Any>?,
    val patient_reminder: Int?,
    val report: Any?,
    val scheduled_at: String?,
    val scheduled_end: String?,
    val service_id: Any?,
    val status: String?,
    val updated_at: String?,
    val driver_rating: Int = 0,
    val patient_rating: Int = 0
) : Serializable