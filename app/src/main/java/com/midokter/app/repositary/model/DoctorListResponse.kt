package com.midokter.app.repositary.model

data class DoctorListResponse(
    val doctor: List<Doctor>,
    val status: Boolean
) {
    data class Doctor(
        val added_by: Int,
        val clinic_id: Int,
        val country_code: Any,
        val created_at: String,
        val deleted_at: Any,
        val email: String,
        val email_token: String,
        val email_verified: Int,
        val email_verified_at: Any,
        val first_name: String,
        val gender: String,
        val id: Int,
        val is_administrative: Int,
        val is_doctor: Int,
        val is_receptionist: Int,
        val is_staff: Int,
        val last_name: String,
        val medical_id: String,
        val mobile: String,
        val otp: Any,
        val regn_id: String,
        val role: Int,
        val services_id: Any,
        val specialities_name: Any,
        val tax_id: String,
        val updated_at: String
    )
}