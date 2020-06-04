package com.midokter.app.repositary.model

data class CategoryResponse(
    val category: List<Category>
) {
    data class Category(
        val created_at: Any,
        val id: Int,
        val name: String,
        val status: Int,
        val updated_at: Any
    )
}