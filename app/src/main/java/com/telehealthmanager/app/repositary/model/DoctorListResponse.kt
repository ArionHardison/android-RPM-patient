package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class DoctorListResponse(
    val Specialities: specialities
) : Serializable {
    data class specialities(
        val doctor_profile: List<DoctorProfile>,
        val id: Int,
        val image: Any,
        val name: String,
        val discount: Double,
        val fees: Double,
        val offer_fees: Double,
        val status: Int
    ): Serializable {
        data class DoctorProfile(
            val address: Any,
            val affiliations: Any,
            val awards: Any,
            val certification: String,
            val city: Any,
            val country: Any,
            val created_at: String,
            val deleted_at: Any,
            val doctor_id: Int,
            val experience: String,
            val fees: Int?=null,
            val gender: String,
            val hospital: List<Hospital>,
            val id: Int,
            val internship: Any,
            val medical_assoc_name: Any,
            val medical_school: Any,
            val postal_code: Any,
            val profile_description: Any,
            val profile_pic: String?=null,
            val profile_video: String,
            val residency: Any,
            val specialities: Int,
            val speciality:Speciality?=null,
            val updated_at: String
        ) : Serializable{
            data class Speciality(
                val id: Int,
                val image: Any,
                val name: String,
                val status: Int
            ) : Serializable
        }
    }
}