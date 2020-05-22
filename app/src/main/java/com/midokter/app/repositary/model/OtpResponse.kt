package com.midokter.doctor.repositary.model

data class OtpResponse(
    val message: String,
    val otp: Int,
    val success: Boolean,
    val status: Boolean
)