package com.telehealthmanager.app.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonSyntaxException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.queenbee.app.session.SessionListener
import com.queenbee.app.session.SessionManager
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.data.NetworkError
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.utils.CustomBackClick
import io.reactivex.disposables.CompositeDisposable
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException


abstract class BaseViewModel<N> : ViewModel(), SessionListener {
    private var compositeDisposable = CompositeDisposable()
    private lateinit var mNavigator: WeakReference<N>
    private var liveErrorResponse = MutableLiveData<String>()
    var toolBarTile = MutableLiveData<String>("")
    var customBackClick: CustomBackClick? = null

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
                getErrorMessage(responseBody!!, e)
            }
            is SocketTimeoutException -> NetworkError.TIME_OUT
            is IOException -> NetworkError.IO_EXCEPTION
            else -> {
                NetworkError.SERVER_EXCEPTION
            }
        }
    }

    private fun getErrorMessage(responseBody: ResponseBody, e: HttpException): String? {
        return try {
            when (e.code()) {
                500 -> NetworkError.SERVER_EXCEPTION
                502 -> NetworkError.BAD_GATE_WAY
                404 -> NetworkError.URL_NOT_FOUND
                405 -> NetworkError.METHOD_NOT_FOUND
                400 -> NetworkError.BAD_REQUEST
                else -> {
                    val jsonObject = JSONObject(responseBody.string())
                    when {
                        jsonObject.has("errors") -> {
                            val errorObject = jsonObject.optJSONObject("errors")
                            when {
                                errorObject!!.has("email") -> errorObject.optJSONArray("email")?.opt(0).toString()
                                errorObject.has("phone") -> errorObject.optJSONArray("phone")?.opt(0).toString()
                                errorObject.has("clinic_email") -> errorObject.optJSONArray("clinic_email")?.opt(0).toString()
                                errorObject.has("payment_mode") -> errorObject.optJSONArray("payment_mode")?.opt(0).toString()
                                else -> getJSONResponse(jsonObject)
                            }
                        }
                        else -> getJSONResponse(jsonObject)
                    }
                }
            }
        } catch (e: Exception) {
            NetworkError.SERVER_EXCEPTION
        }
    }

    private fun getJSONResponse(jsonObject: JSONObject): String? {
        return try {
            when {
                jsonObject.has("error") -> jsonObject.getString("error")
                jsonObject.has("message") -> jsonObject.getString("message")
                else -> NetworkError.SERVER_EXCEPTION
            }
        } catch (e: Exception) {
            NetworkError.SERVER_EXCEPTION
        }
    }
}
