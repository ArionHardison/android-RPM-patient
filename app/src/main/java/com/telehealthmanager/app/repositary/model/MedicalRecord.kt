package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class MedicalRecord(
    val medical: List<Medical>,
    val status: Int
) : Serializable {
    data class Medical(
        val added_by: Int?,
        val clinic_id: Int?,
        val country_code: Any?,
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
        val subscribe_from: Any?,
        val subscribe_limit: Any?,
        val subscribe_to: Any?,
        val tax_id: String?,
        val updated_at: String?,
        val wallet_balance: Any?,
        val doctor_profile: DoctorProfile
    ) : Serializable {
        data class DoctorProfile(
            val address: Any,
            val affiliations: Any,
            val awards: Any,
            val certification: Any,
            val city: Any,
            val country: Any,
            val created_at: String,
            val deleted_at: Any,
            val doctor_id: Int,
            val experience: String,
            val fees: Int,
            val gender: String,
            val hospital: List<Hospital>,
            val id: Int,
            val internship: Any,
            val medical_assoc_name: Any,
            val medical_school: Any,
            val postal_code: Any,
            val profile_description: Any,
            val profile_pic: String,
            val residency: Any,
            val specialities: Any,
            val speciality: Speciality,
            val updated_at: String
        ) : Serializable {
            data class Speciality(
                val id: Int,
                val image: Any,
                val name: String,
                val status: Int
            ) : Serializable
        }

        data class Clinic(
            val address: String,
            val country_code: Any,
            val created_at: String,
            val deleted_at: Any,
            val doctor_id: Int,
            val email: String,
            val id: Int,
            val latitude: Any,
            val longitude: Any,
            val mobile: Any,
            val name: String,
            val phone: String,
            val postal_code: String,
            val updated_at: String
        ) : Serializable
    }

}







