package com.midokter.app.repositary

import com.midokter.app.repositary.model.ProfileResponse
import com.midokter.app.repositary.model.RegisterResponse
import com.midokter.app.repositary.model.Response
import com.midokter.doctor.repositary.model.LoginResponse

import com.midokter.doctor.repositary.model.*

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiInterface {

    @FormUrlEncoded
    @POST("api/patient/signup")
    fun signup(@FieldMap hashMap: HashMap<String, Any>): Observable<RegisterResponse>


    @FormUrlEncoded
    @POST("api/patient/verify_otp")
    fun signIn(@FieldMap hashMap: HashMap<String, Any>): Observable<LoginResponse>


    @FormUrlEncoded
    @POST("api/patient/otp")
    fun Sendotp(@FieldMap hashMap: HashMap<String, Any>): Observable<OtpResponse>

    @FormUrlEncoded
    @POST("api/patient/verify_otp")
    fun verifyotp(@FieldMap hashMap: HashMap<String, Any>): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("api/patient/logout")
    fun logout(@FieldMap hashMap: HashMap<String, Any>): Observable<Response>

    @GET("api/patient/profile")
    fun getProfile(): Observable<ProfileResponse>

    @FormUrlEncoded
    @POST("api/hospital/update_profile")
    fun updateprofile(@FieldMap hashMap: HashMap<String, Any>): Observable<OtpResponse>


    @Multipart
    @POST("api/hospital/update_profile")
    fun profileUpdate(@PartMap params: HashMap<String, RequestBody>, @Part image : MultipartBody.Part): Observable<OtpResponse>


}