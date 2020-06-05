package com.midokter.app.repositary.model

data class FeedbackResponse(
    val feedback: Feedback,
    val message: String
) {
    data class Feedback(
        val comments: String,
        val created_at: String,
        val experiences: String,
        val hospital_id: String,
        val id: Int,
        val patient_id: Int,
        val updated_at: String,
        val visited_for: String
    )
}