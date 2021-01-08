package com.telehealthmanager.app.ui.timeslot

import java.io.Serializable

data class SloItem(
    var session: String = "",
    var sessionTimeList: List<SlotTimeItem>? = null
) : Serializable