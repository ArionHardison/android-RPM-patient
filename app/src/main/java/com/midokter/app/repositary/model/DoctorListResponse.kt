package com.midokter.app.repositary.model

import java.io.Serializable

data class DoctorListResponse(
    val Specialities: specialities
) : Serializable {
    data class specialities(
        val doctor_profile: List<DoctorProfile>,
        val id: Int,
        val image: Any,
        val name: String,
        val status: Int
    ): Serializable {
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
            val specialities: Int,
            val updated_at: String
        ) : Serializable/*{
            data class Hospital(
                val added_by: Any,
                val clinic: Clinic,
                val clinic_id: Int,
                val country_code: Any,
                val created_at: String,
                val deleted_at: Any,
                val doctor_service: List<DoctorService>,
                val email: String,
                val email_token: String,
                val email_verified: Int,
                val email_verified_at: Any,
                val feedback: List<Feedback>,
                val feedback_percentage: String,
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
                val services_id: String,
                val specialities_name: String,
                val tax_id: String,
                val timing: List<Timing>,
                val updated_at: String
            ) : Serializable{
                data class Clinic(
                    val address: String,
                    val clinic_photo: List<Any>,
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

                data class DoctorService(
                    val hospital_id: Int,
                    val id: Int,
                    val service: Service,
                    val service_id: Int
                ) : Serializable{
                    data class Service(
                        val id: Int,
                        val name: String,
                        val status: Int
                    ): Serializable
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
                ): Serializable

                data class Timing(
                    val created_at: Any,
                    val day: String,
                    val deleted_at: Any,
                    val doctor_id: Int,
                    val end_time: String,
                    val id: Int,
                    val start_time: String,
                    val updated_at: Any
                ): Serializable
            }
        }*/
    }
}