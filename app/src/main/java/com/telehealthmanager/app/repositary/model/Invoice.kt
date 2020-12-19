package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class Invoice(
    val appointment_id: Int?,
    val commission: String = "",
    val consulting_fees: String = "",
    val created_at: String?,
    val deleted_at: Any?,
    val discount: String = "",
    val gross: String = "",
    val id: Int?,
    val paid: String = "",
    val payment_mode: String?,
    val promocode_amount: Int?,
    val promocode_id: Int?,
    val speciality_fees:String = "",
    val status: String?,
    val total_pay: String = "",
    val updated_at: String?
) : Serializable