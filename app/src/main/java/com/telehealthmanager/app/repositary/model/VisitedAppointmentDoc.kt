package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class VisitedAppointmentDoc(
    val visited_doctors: VisitedDoctors?
): Serializable