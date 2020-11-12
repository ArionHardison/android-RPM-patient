package com.telehealthmanager.app.repositary.model

data class HomeResponse(
    val Specialities: List<Speciality>,
    val favourite_Doctors: List<Any>,
    val search_doctors: List<Any>,
    val visited_doctors: List<Any>
) {
    data class Speciality(
        val doctor_profile: List<Any>,
        val id: Int,
        val name: String,
        val status: Int
    )
    data class Menu(
        val imgresouce: Int,
        val name: String,
        val subname: String
    )
}