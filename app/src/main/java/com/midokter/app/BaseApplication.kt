package com.midokter.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.multidex.MultiDex
import com.facebook.stetho.Stetho
import com.midokter.app.data.Constant
import com.midokter.app.di.component.BaseComponent
import com.midokter.app.di.component.DaggerBaseComponent
import com.midokter.app.di.modules.NetworkModule


open class BaseApplication : Application() {

    val baseComponent: BaseComponent = DaggerBaseComponent.builder()
        .networkModule(NetworkModule())
        .build()

    override fun onCreate() {
        super.onCreate()
        Stetho.initializeWithDefaults(this)
        baseApplication = this
        preferences = getSharedPreferences(Constant.CUSTOM_PREFERENCE, Context.MODE_PRIVATE)
    }

    companion object {
        lateinit var baseApplication: Context
        private lateinit var preferences: SharedPreferences
        val getCustomPreference: SharedPreferences? get() = preferences
        private val monitorInternetLiveData = MutableLiveData<Boolean>()
        val getInternetMonitorLiveData: MutableLiveData<Boolean> get() = monitorInternetLiveData
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}