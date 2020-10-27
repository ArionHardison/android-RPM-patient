package com.telehealthmanager.app.repositary.model

data class Response(
    val message: String,
    val status: Boolean,
    val is_favourite: Boolean
)