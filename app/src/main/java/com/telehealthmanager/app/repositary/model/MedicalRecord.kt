package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class MedicalRecord(
    val medical: List<Medical>,
    val status: Int
) : Serializable

data class Medical(
    val appointment_type: String,
    val booking_for: String,
    val checkedout_at: String,
    val checkin_at: String,
    val consult_time: String,
    val created_at: String,
    val deleted_at: Any,
    val description: String,
    val doctor_id: Int,
    val doctor_reminder: Int,
    val engaged_at: String,
    val hospital: Hospital1,
    val id: Int,
    val patient_files: List<Any>,
    val patient_id: Int,
    val patient_reminder: Int,
    val report: Any,
    val scheduled_at: String,
    val service_id: Int,
    val status: String,
    val updated_at: String
) : Serializable


data class Hospital1(
    val added_by: Int,
    val clinic: Clinic,
    val clinic_id: Any,
    val country_code: Any,
    val created_at: String,
    val deleted_at: Any,
    val doctor_profile: DoctorProfile,
    val email: String,
    val email_token: String,
    val email_verified: Int,
    val email_verified_at: Any,
    val first_name: String,
    val gender: Any,
    val id: Int,
    val is_administrative: Int,
    val is_doctor: Int,
    val is_receptionist: Int,
    val is_staff: Int,
    val last_name: String,
    val medical_id: String,
    val mobile: String,
    val otp: Any,
    val regn_id: String,
    val role: Any,
    val services_id: Any,
    val specialities_name: Any,
    val tax_id: String,
    val updated_at: String
) : Serializable

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
) : Serializable

data class Speciality(
    val id: Int,
    val image: Any,
    val name: String,
    val status: Int
) : Serializable


