package com.telehealthmanager.app.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonSyntaxException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.data.NetworkError
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.queenbee.app.session.SessionListener
import com.queenbee.app.session.SessionManager
import com.telehealthmanager.app.utils.CustomBackClick
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

abstract class BaseViewModel<N> : ViewModel() , SessionListener {
    private var compositeDisposable = CompositeDisposable()
    private lateinit var mNavigator: WeakReference<N>
    private var liveErrorResponse = MutableLiveData<String>()
    var toolBarTile = MutableLiveData<String>("")
    var customBackClick:CustomBackClick? = null

    fun setOnClickListener(onViewClickListener: CustomBackClick) {
        this.customBackClick = onViewClickListener
    }

    var navigator: N
        get() = mNavigator.get()!!
        set(navigator) {
            this.mNavigator = WeakReference(navigator)
        }

    fun getCompositeDisposable() = compositeDisposable

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getErrorObservable(): MutableLiveData<String> {
        return liveErrorResponse
    }

    fun clickBackPress() {
        customBackClick!!.clickBackPress()
    }

    override fun invalidate() {

    }

    override fun refresh() {
    }

    fun getErrorMessage(e: Throwable): String? {
        return when (e) {
            is JsonSyntaxException -> {
                if (BuildConfig.DEBUG)
                    e.message.toString()
                else
                    NetworkError.DATA_EXCEPTION
            }
            is HttpException -> {
                val responseBody = e.response().errorBody()
                if (e.code() == 401 && PreferenceHelper(BaseApplication.baseApplication).getValue(
                        PreferenceKey.ACCESS_TOKEN,
                        ""
                    )!! != ""
                ) {
                    SessionManager.instance(this)
                    SessionManager.clearSession()
                }
                getErrorMessage(responseBody!!)
            }
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            else -> {
                NetworkError.SERVER_EXCEPTION
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody): String? {
        return try {
            val jsonObject = JSONObject(responseBody.string())
            if(jsonObject.has("error"))
                jsonObject.getString("error")
            else if(jsonObject.has("message"))
                jsonObject.getString("message")
            else
                NetworkError.SERVER_EXCEPTION
        } catch (e: Exception) {
            NetworkError.SERVER_EXCEPTION
        }
    }
}
