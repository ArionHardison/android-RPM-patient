package com.telehealthmanager.app.ui.timeslot

import java.io.Serializable
import java.util.*

data class SlotSelectionItem(
    var day: String = "",
    var date1: Date? = null,
    var selectedItem: Boolean = false,
    var date: String = "",
    var slotSelectionItem: ArrayList<SloItem>? = null
) : Serializable