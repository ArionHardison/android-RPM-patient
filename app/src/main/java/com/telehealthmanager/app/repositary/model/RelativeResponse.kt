package com.telehealthmanager.app.repositary.model

data class RelativeResponse(
    val relatives: List<RelativeList>?,
    val relative_detail: RelativeList?,
    val status: Int?
)