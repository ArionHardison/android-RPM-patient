package com.telehealthmanager.app.repositary.model

import java.io.Serializable

data class CardList(
    val brand: String?,
    val card_id: String?,
    val created_at: String?,
    val deleted_at: Any?,
    val id: Int?,
    val is_default: Int?,
    val last_four: String?,
    val type: Any?,
    val updated_at: String?,
    val user_id: Int?,
    var cardSelect: Boolean = false
) : Serializable