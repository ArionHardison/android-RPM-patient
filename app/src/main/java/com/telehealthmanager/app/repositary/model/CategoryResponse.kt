package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class CategoryResponse(
    val category: List<Category>
):Serializable {
    data class Category(
        val created_at: Any,
        val id: Int,
        val name: String,
        val image: String?,
        val status: Int,
        val discount: Double,
        val fees: Double,
        val offer_fees: Double,
        val updated_at: Any
    ):Serializable
}