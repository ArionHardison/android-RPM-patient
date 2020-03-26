package com.midokter.app.di.modules

import android.util.Log
import androidx.preference.PreferenceManager
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.data.Constant.M_TOKEN
import com.midokter.app.data.PreferenceKey
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    //.cache(new Cache(MvpApplication.getInstance().getCacheDir(), 10 * 1024 * 1024)) // 10 MB
    val httpClient: OkHttpClient
        @Provides
        @Singleton
        get() {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            return OkHttpClient()
                .newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(AddHeaderInterceptor())
                .addNetworkInterceptor(StethoInterceptor())
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build()
        }

    @Provides
    @Singleton
    fun providesRetrofitService(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    inner class AddHeaderInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder = chain.request().newBuilder()
            builder.addHeader("X-Requested-With", "XMLHttpRequest")
            val token = Objects.requireNonNull(
                PreferenceManager.getDefaultSharedPreferences(BaseApplication.baseApplication).getString(
                    PreferenceKey.ACCESS_TOKEN,
                    ""
                )
            )
            builder.addHeader("Authorization", M_TOKEN + token)
            Log.d("TTT access_token", token)
            return chain.proceed(builder.build())
        }
    }
}