package com.midokter.app.repositary

import com.midokter.app.repositary.model.*
import com.midokter.app.repositary.model.chatmodel.ChatListResponse
import com.midokter.app.repositary.model.chatmodel.ChatStatusResponse
import com.midokter.app.ui.twilio.TwilloVideoActivity
import com.midokter.doctor.repositary.model.LoginResponse

import com.midokter.doctor.repositary.model.*

import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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
    @POST("api/patient/booking")
    fun bookdoctor(@FieldMap hashMap: HashMap<String, Any>): Observable<BookedResponse>

    @FormUrlEncoded
    @POST("api/patient/logout")
    fun logout(@FieldMap hashMap: HashMap<String, Any>): Observable<Response>

    @GET("api/patient/profile")
    fun getProfile(): Observable<ProfileResponse>

/*home*/
    @GET("api/patient/search_doctor")
    fun getHome(@QueryMap hashMap: HashMap<String, Any>): Observable<MainResponse>

  /*  doctor*/
    @GET("api/patient/doctor_catagory")
    fun getCategorys(): Observable<CategoryResponse>

    @GET("api/patient/doctor_catagory/{id}")
    fun getDoctorByCategorys(@Path("id") id:Int?): Observable<DoctorListResponse>

    @FormUrlEncoded
    @POST("api/hospital/update_profile")
    fun updateprofile(@FieldMap hashMap: HashMap<String, Any>): Observable<OtpResponse>


    @Multipart
    @POST("api/hospital/update_profile")
    fun profileUpdate(@PartMap params: HashMap<String, RequestBody>, @Part image : MultipartBody.Part): Observable<OtpResponse>

   /* Appointment*/
   @GET("api/patient/appointment")
   fun getAppointment(): Observable<AppointmentResponse>

    @FormUrlEncoded
    @POST("api/patient/cancel_appointment")
    fun cancelAppointment(@FieldMap hashMap: HashMap<String, Any>): Observable<Response>

    /*visted details*/
    @FormUrlEncoded
    @POST("api/patient/feedback")
    fun postfeedback(@FieldMap hashMap: HashMap<String, Any>): Observable<FeedbackResponse>

    /*doctor profile*/
    @FormUrlEncoded
    @POST("api/patient/favourite_doctor")
    fun addfav(@FieldMap hashMap: HashMap<String, Any>): Observable<Response>

    /* Appointment*/
    @GET("api/patient/medical_records")
    fun getMedicalRecord(): Observable<MedicalRecord>

    @GET("api/patient/articles")
    fun getArticles(): Observable<ArticleResponse>

    @FormUrlEncoded
    @POST("api/patient/profile")
    fun editPatient(
        @FieldMap hashMap: HashMap<String, Any>
    ): Observable<ProfileResponse>

    @GET("api/patient/chat/history")
    fun getChat(): Observable<ChatListResponse>

    @GET("api/patient/chat/status/{id}")
    fun getChatStatus(@Path("id") int: Int): Observable<ChatStatusResponse>

    @Multipart
    @POST("api/patient/profile")
    fun editPatientWithImage(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Observable<ProfileResponse>

    @FormUrlEncoded
    @POST("api/patient/add_wallet")
    fun addMoneyToWallet(@FieldMap hashMap: HashMap<String, Any>): Observable<WalletResponse>

   /* search*/
   @GET("api/patient/home_search")
   fun getglobalsearch(@QueryMap hashMap: HashMap<String, Any>): Observable<SearchResponse>



    @FormUrlEncoded
    @POST("api/patient/reminder")
    fun addRemainder(@FieldMap hashMap: HashMap<String, Any>): Observable<AddRemainderResponse>


    @GET("api/patient/reminder_list")
    fun getReminders(): Observable<ReminderResponse>

    @FormUrlEncoded
    @POST("api/patient/chat/promocode")
    fun addChatPromoCode(@FieldMap hashMap: HashMap<String, Any>): Observable<ChatPromoResponse>

    @FormUrlEncoded
    @POST("api/patient/payment")
    fun payForChatRequest(@FieldMap hashMap: HashMap<String, Any>): Observable<MessageResponse>



    @GET("api/patient/video/call/check")
    fun videoCheckStatusAPI(): Observable<VideoStatusCheck>

    @GET("api/patient/video/cancel")
    fun cancelVideoCall(
        @Query("user_id") user_id: Any?,
        @Query("provider_id") provider_id: Any?,
        @Query("push_to") push_to: Any?
    ): Call<TwilloVideoActivity.AccessToken?>?

    @GET("api/patient/video/call/token")
    fun getTwilloVideoToken(
        @Query("room_id") obj: Any?
    ): Call<TwilloVideoActivity.AccessToken?>?

}