package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class AppointmentResponse(
    val previous: Previous,
    val upcomming: Upcomming
) : Serializable {
    data class Previous(
        val appointments: List<Appointment>,
        val created_at: String,
        val deleted_at: Any,
        val device_id: Any,
        val device_token: Any,
        val device_type: String,
        val email: String,
        val email_token: Any,
        val email_verified: Int,
        val email_verified_at: Any,
        val first_name: String,
        val id: Int,
        val last_name: String,
        val login_by: String,
        val other_id: Any,
        val otp: Int,
        val payment_mode: String,
        val phone: String,
        val rating: String,
        val regn_id: Any,
        val secondary_mobile: Any,
        val social_unique_id: Any,
        val updated_at: String,
        val wallet_balance: Int
    ) : Serializable

    data class Upcomming(
        val appointments: List<Appointment>,
        val created_at: String,
        val deleted_at: Any,
        val device_id: Any,
        val device_token: Any,
        val device_type: String,
        val email: String,
        val email_token: Any,
        val email_verified: Int,
        val email_verified_at: Any,
        val first_name: String,
        val id: Int,
        val last_name: String,
        val login_by: String,
        val other_id: Any,
        val otp: Int,
        val payment_mode: String,
        val phone: String,
        val rating: String,
        val regn_id: Any,
        val secondary_mobile: Any,
        val social_unique_id: Any,
        val updated_at: String,
        val wallet_balance: Int
    ) : Serializable
}