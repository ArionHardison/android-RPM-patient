package com.midokter.app.repositary

import com.google.gson.JsonSyntaxException
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.data.NetworkError
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.getValue
import com.queenbee.app.session.SessionManager
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
            jsonObject.getString("error")
        } catch (e: Exception) {
            e.message
        }
    }


}
