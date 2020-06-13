package com.midokter.app.repositary.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ArticleResponse(
    @SerializedName("article")
    val article: List<Article> = listOf()
):Serializable {
    data class Article(
        @SerializedName("cover_photo")
        val coverPhoto: String? = "",
        @SerializedName("created_at")
        val createdAt: String = "",
        @SerializedName("description")
        val description: String = "",
        @SerializedName("hospital_id")
        val hospitalId: Int = 0,
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("name")
        val name: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("updated_at")
        val updatedAt: String = ""
    ):Serializable
}