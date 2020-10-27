package com.telehealthmanager.app.ui.activity.addreminder

interface AddReminderNavigator {
    fun onFromDateClicked()
    fun onFromTimeClicked()
    fun validateAddRemainder()
    fun onBackButtonClicked()
}