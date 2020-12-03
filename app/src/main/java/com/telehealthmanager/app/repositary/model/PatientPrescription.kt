package com.telehealthmanager.app.repositary.model

data class PatientPrescription(
    val created_at: String?,
    val doctor_id: String?,
    val `file`: String?,
    val id: Int?,
    val instruction: String?,
    val patient_id: String?,
    val record_date: String?,
    val title: String?,
    val updated_at: String?
)