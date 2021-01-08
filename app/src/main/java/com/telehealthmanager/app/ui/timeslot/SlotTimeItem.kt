package com.telehealthmanager.app.ui.timeslot

import java.io.Serializable

data class SlotTimeItem(
    var isSelected: Boolean = false,
    val time: String = ""
) : Serializable