package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class MainResponse(
    val favourite_Doctors: List<Doctor>,
    val search_doctors: List<Hospital>,
    val visited_doctors: List<VisitedDoctor>
) : Serializable {

    data class Doctor(
        val id: Int,
        val hospital: Hospital?=null,
        val hospital_id: Int,
        val patient_id: Int
    ): Serializable


    data class VisitedDoctor(
        val appointment_type: String,
        val checkedout_at: Any,
        val checkin_at: String,
        val consult_time: String,
        val created_at: String,
        val deleted_at: Any,
        val description: String,
        val booking_for: String,
        val doctor_id: Int,
        val doctor_reminder: Int,
        val engaged_at: Any,
        val hospital: Hospital,
        val id: Int,
        val patient_id: Int,
        val patient_reminder: Int,
        val report: Any,
        val scheduled_at: String,
        val service_id: Int,
        val status: String,
        val updated_at: String
    ) : Serializable{
        data class Hospital(
            val added_by: Any,
            val clinic: Clinic,
            val clinic_id: Int,
            val country_code: Any,
            val created_at: String,
            val deleted_at: Any,
            val doctor_profile: DoctorProfile,
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
            val otp: Int,
            val regn_id: String,
            val role: Int,
            val services_id: Any,
            val specialities_name: String,
            val feedback: List<Feedback>,
            val timing: List<Timing>,
            val tax_id: String,
            val updated_at: String
        ): Serializable {
            data class Clinic(
                val address: String,
                val country_code: Any,
                val created_at: String,
                val deleted_at: Any,
                val doctor_id: Int,
                val email: String,
                val id: Int,
                val latitude: Double,
                val longitude: Double,
                val mobile: Any,
                val name: String,
                val phone: String,
                val postal_code: String,
                val updated_at: String
            ): Serializable

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
                val experience: Any,
                val fees: Int,
                val gender: String,
                val id: Int,
                val internship: Any,
                val medical_assoc_name: Any,
                val medical_school: Any,
                val postal_code: Any,
                val profile_description: Any,
                val profile_pic: String,
                val residency: Any,
                val specialities: Int,
                val speciality: Speciality,
                val updated_at: String
            ): Serializable {
                data class Speciality(
                    val id: Int,
                    val image: Any,
                    val name: String,
                    val status: Int
                ) : Serializable
            }

            data class Feedback(
                val comments: String,
                val created_at: String,
                val experiences: String,
                val hospital_id: Int,
                val id: Int,
                val patient_id: Int,
                val updated_at: String,
                val visited_for: String
            ) : Serializable

            data class Timing(
                val created_at: Any,
                val day: String,
                val deleted_at: Any,
                val doctor_id: Int,
                val end_time: String,
                val id: Int,
                val start_time: String,
                val updated_at: Any
            ) : Serializable
        }
    }
}