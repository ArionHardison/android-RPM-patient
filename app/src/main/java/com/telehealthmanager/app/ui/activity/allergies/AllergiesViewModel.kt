package com.telehealthmanager.app.ui.activity.allergies

import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.model.Hospital

class AllergiesViewModel :BaseViewModel<AllergiesNavigator>() {


    fun clickAddAllergies(){
        navigator.openAddAllergies()
    }

    fun clickNoAllergies(){
        navigator.noAllergies()
    }
}