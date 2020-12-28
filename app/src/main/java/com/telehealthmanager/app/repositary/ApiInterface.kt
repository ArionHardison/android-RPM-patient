package com.telehealthmanager.app.repositary

import com.telehealthmanager.app.repositary.model.*
import com.telehealthmanager.app.repositary.model.chatmodel.ChatListResponse
import com.telehealthmanager.app.repositary.model.chatmodel.ChatStatusResponse
import com.telehealthmanager.app.ui.twilio.model.AccessToken
import com.telehealthmanager.doctor.repositary.model.LoginResponse
import com.telehealthmanager.doctor.repositary.model.OtpResponse
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

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
    @POST("api/patient/change_password")
    fun updatePassword(@FieldMap hashMap: HashMap<String, Any>): Observable<Objects>

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

    @GET("api/patient/search_doctor")
    suspend fun getDoctorsList(@Query("page") page: Int): MainResponse

    @GET("api/patient/search_doctor")
    suspend fun getDoctorsList( @Query("page") page: Int,@Query("search") search: String): MainResponse

    /*  doctor*/
    @GET("api/patient/doctor_catagory")
    fun getCategorys(): Observable<CategoryResponse>

    @GET("api/patient/doctor_catagory/{id}")
    fun getDoctorByCategorys(@Path("id") id: Int?,@QueryMap hashMap: HashMap<String, Any>): Observable<DoctorListResponse>

    @GET("api/patient/doctor_catagory/{id}")
    fun getDoctorFilterByCategories(@Path("id") id: Int?, @QueryMap hashMap: HashMap<String, Any>): Observable<DoctorListResponse>

    /* Appointment*/
    @GET("api/patient/appointment")
    fun getAppointment(): Observable<AppointmentResponse>

    @GET("api/patient/visited_doctors")
    fun getVisitedDoc(): Observable<VisitedAppointmentDoc>

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

    /* TODO Medical Record*/
    @GET("api/patient/records_list")
    fun getMedicalRecord(): Observable<MedicalRecord>

    @Multipart
    @POST("api/patient/medical_records")
    fun addMedicalRecords(@PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Observable<ResponsePrescription>

    @GET("api/patient/doctor")
    fun getAllDoctors(): Observable<ResponseDoctors>

    @GET("api/patient/records_details/{record_id}")
    fun getRecordsList(@Path("record_id") id: String?): Observable<ResponseMedicalDetails>

    @GET("api/patient/articles")
    fun getArticles(): Observable<ArticleResponse>

    @FormUrlEncoded
    @POST("api/patient/profile")
    fun editPatient(
        @FieldMap hashMap: HashMap<String, Any>
    ): Observable<ProfileResponse>


    /*TODO RELATIVE*/
    @GET("api/patient/relative/list")
    fun getRelativeList(@QueryMap hashMap: HashMap<String, Any>): Observable<RelativeResponse>

    @GET("api/patient/relative/{id}")
    fun getSingleRelative(@Path("id") id: String?): Observable<RelativeResponse>

    @Multipart
    @POST("api/patient/relative")
    fun addPatientRelative(@PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Observable<AddUpdateRelative>

    @FormUrlEncoded
    @POST("api/patient/relative")
    fun addPatientRelative(@FieldMap params: HashMap<String, Any>): Observable<AddUpdateRelative>

    @Multipart
    @POST("api/patient/relative/{id}")
    fun updateRelativePatient(@Path("id") id: String?, @PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Observable<AddUpdateRelative>

    @FormUrlEncoded
    @POST("api/patient/relative/{id}")
    fun updateRelativePatient(@Path("id") id: String?, @FieldMap params: HashMap<String, Any>): Observable<AddUpdateRelative>

    @Multipart
    @POST("api/patient/profile")
    fun editPatientWithImage(
        @PartMap hashMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Observable<ProfileResponse>

    /*TODO CHAT*/
    @GET("api/patient/chat/history")
    fun getChat(): Observable<ChatListResponse>

    @GET("api/patient/chat/status/{id}")
    fun getChatStatus(@Path("id") int: Int): Observable<ChatStatusResponse>

    @GET("api/patient/chat_push")
    fun postChat(@QueryMap hashMap: HashMap<String, String>): Call<Object>

    /*TODO SEARCH*/
    @GET("api/patient/home_search")
    fun getGlobalSearchApp(@QueryMap hashMap: HashMap<String, Any>): Observable<SearchResponse>


    @FormUrlEncoded
    @POST("api/patient/reminder")
    fun addRemainder(@FieldMap hashMap: HashMap<String, Any>): Observable<AddRemainderResponse>


    @GET("api/patient/reminder_list")
    fun getReminders(): Observable<ReminderResponse>

    @FormUrlEncoded
    @POST("api/patient/chat/promocode")
    fun addChatPromoCode(@FieldMap hashMap: HashMap<String, Any>): Observable<ChatPromoSuccess>

    @FormUrlEncoded
    @POST("api/patient/payment")
    fun payForChatRequest(@FieldMap hashMap: HashMap<String, Any>): Observable<MessageResponse>


    @GET("api/patient/video/call/check")
    fun videoCheckStatusAPI(): Observable<VideoStatusCheck>

    /* TODO TWILIO CALL */
    @GET("api/patient/video/cancel")
    fun cancelVideoCall(@QueryMap hashMap: HashMap<String, Any>): Call<VideoCallCancelResponse>?

    @GET("api/patient/video/call/token")
    fun getTwilloVideoToken(@QueryMap hashMap: HashMap<String, Any>): Call<AccessToken>

    @GET("api/patient/video/call")
    fun getCallRequest(@QueryMap hashMap: HashMap<String, Any>): Call<AccessToken>


    /* TODO CARD */
    @GET("api/patient/card?user_type=patient")
    fun getCards(): Observable<List<CardList>>

    @FormUrlEncoded
    @POST("api/patient/card")
    fun addCardDetails(@FieldMap hashMap: HashMap<String, Any>): Observable<CardSuccessMessage>

    @FormUrlEncoded
    @POST("api/patient/delete/card")
    fun deleteCardDetails(@FieldMap hashMap: HashMap<String, Any>): Observable<CardSuccessMessage>

    @FormUrlEncoded
    @POST("api/patient/add_money")
    fun addMoney(@FieldMap hashMap: HashMap<String, Any>): Observable<WalletAddSuccess>


    @GET("api/patient/faq")
    fun getFaqList(): Observable<FaqResponse>
}