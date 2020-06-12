package com.midokter.app.repositary.model

data class ProfileResponse(
    val patient: Patient,
    val profile_complete: String
) {
    data class Patient(
        val created_at: String,
        val deleted_at: Any,
        val email: String,
        val email_token: String,
        val email_verified: Int,
        val email_verified_at: Any,
        val first_name: String,
        val id: Int,
        val last_name: String,
        val otp: Int,
        val phone: String,
        val profile: Profile,
        val regn_id: String,
        val wallet_balance: String,
        val updated_at: String
    ) {
        data class Profile(
            val activity: Any,
            val address: String,
            val age: String,
            val alcohol: Any,
            val allergies: Any,
            val blood_group: String,
            val chronic_diseases: Any,
            val city: Any,
            val country: Any,
            val created_at: String,
            val current_medications: Any,
            val deleted_at: Any,
            val description: String,
            val dob: String,
            val emergency_contact: Any,
            val food: Any,
            val gender: String,
            val groups: Any,
            val height: Any,
            val id: Int,
            val injuries: Any,
            val merital_status: Any,
            val occupation: Any,
            val past_medications: Any,
            val patient_id: String,
            val postal_code: Any,
            val profile_pic: Any,
            val refered_by: Any,
            val smoking: Any,
            val surgeries: Any,
            val updated_at: String,
            val weight: Any
        )
    }
}