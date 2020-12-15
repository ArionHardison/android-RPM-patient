package com.telehealthmanager.app.repositary

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
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BaseRepository {

    @Inject
    lateinit var retrofit: Retrofit

    init {
        BaseApplication().baseComponent.inject(this)
    }

    @Singleton
    fun reconstructedRetrofit(baseUrl: String): Retrofit {
        return retrofit.newBuilder()
            .baseUrl("$baseUrl")
            .build()
    }

    fun <T> createApiClient(service: Class<T>): T {
        return retrofit.create(service)
    }

    fun <T> createApiClient(baseUrl: String, service: Class<T>): T {
        println("create api client url $baseUrl")
        return reconstructedRetrofit(baseUrl).create(service)
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
                    SessionManager.instance(object : SessionListener {
                        override fun invalidate() {

                        }

                        override fun refresh() {
                        }
                    })
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
            when {
                jsonObject.has("errors") -> {
                    val errorObject = jsonObject.optJSONObject("errors")
                    when {
                        errorObject!!.has("email") -> errorObject.optJSONArray("email")?.opt(0).toString()
                        errorObject.has("phone") -> errorObject.optJSONArray("phone")?.opt(0).toString()
                        errorObject.has("prescription_image") -> errorObject.optJSONArray("prescription_image")?.opt(0).toString()
                        errorObject.has("payment_mode") -> errorObject.optJSONArray("payment_mode")?.opt(0).toString()
                        else -> getJSONResponse(jsonObject)
                    }
                }
                else -> getJSONResponse(jsonObject)
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
