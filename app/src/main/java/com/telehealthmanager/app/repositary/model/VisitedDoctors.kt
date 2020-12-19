package com.telehealthmanager.app.repositary.model

import com.telehealthmanager.app.repositary.model.Appointment
import java.io.Serializable

data class VisitedDoctors(
    val appointments: MutableList<Appointment>?,
    val created_at: String?,
    val deleted_at: Any?,
    val device_id: String?,
    val device_token: String?,
    val device_type: String?,
    val email: String?,
    val email_token: String?,
    val email_verified: Int?,
    val email_verified_at: Any?,
    val first_name: String?,
    val id: Int?,
    val last_name: String?,
    val login_by: String?,
    val other_id: Any?,
    val otp: Int?,
    val payment_mode: String?,
    val phone: String?,
    val rating: String?,
    val regn_id: String?,
    val secondary_mobile: Any?,
    val social_unique_id: Any?,
    val stripe_cust_id: String?,
    val updated_at: String?,
    val wallet_balance: Any?
): Serializable