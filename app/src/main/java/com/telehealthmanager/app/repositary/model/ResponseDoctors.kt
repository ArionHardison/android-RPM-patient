package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class ResponseDoctors(
    val doctor: List<AllDoctors>?,
    val status: Boolean?
) : Serializable {
    data class AllDoctors(
        val added_by: Int?,
        val clinic_id: Int?,
        val country_code: Any?,
        val created_at: String?,
        val deleted_at: Any?,
        val device_id: Any?,
        val device_token: Any?,
        val device_type: String?,
        val email: String?,
        val email_token: String?,
        val email_verified: Int?,
        val email_verified_at: Any?,
        val first_name: String?,
        val gender: String?,
        val id: Int?,
        val is_administrative: Int?,
        val is_doctor: Int?,
        val is_receptionist: Int?,
        val is_staff: Int?,
        val last_name: String?,
        val login_by: String?,
        val medical_id: String?,
        val mobile: String?,
        val otp: Any?,
        val push_device_token: Any?,
        val rating: String?,
        val regn_id: String?,
        val role: Int?,
        val services_id: Any?,
        val social_unique_id: Any?,
        val specialities_name: Any?,
        val status: String?,
        val subscribe_from: String?,
        val subscribe_limit: Any?,
        val subscribe_to: Any?,
        val tax_id: String?,
        val updated_at: String?,
        val wallet_balance: Int?
    ) : Serializable
}