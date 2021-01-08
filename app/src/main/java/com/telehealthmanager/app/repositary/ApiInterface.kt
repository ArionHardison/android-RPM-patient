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
    fun apiSignUp(@FieldMap hashMap: HashMap<String, Any>): Observable<RegisterResponse>

    @FormUrlEncoded
    @POST("api/patient/verify_otp")
    fun apiSignIn(@FieldMap hashMap: HashMap<String, Any>): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("api/patient/otp")
    fun apiSendOtp(@FieldMap hashMap: HashMap<String, Any>): Observable<OtpResponse>

    @FormUrlEncoded
    @POST("api/patient/verify_otp")
    fun apiVerifyOtp(@FieldMap hashMap: HashMap<String, Any>): Observable<LoginResponse>

    @FormUrlEncoded
    @POST("api/patient/change_password")
    fun apiUpdatePassword(@FieldMap hashMap: HashMap<String, Any>): Observable<Objects>

    @FormUrlEncoded
    @POST("api/patient/booking")
    fun apiBookDoctor(@FieldMap hashMap: HashMap<String, Any>): Observable<BookedResponse>

    @FormUrlEncoded
    @POST("api/patient/logout")
    fun apiLogOut(@FieldMap hashMap: HashMap<String, Any>): Observable<Response>

    @GET("api/patient/profile")
    fun apiGetProfile(): Observable<ProfileResponse>

    /*home*/
    @GET("api/patient/search_doctor")
    fun apiGetHome(@QueryMap hashMap: HashMap<String, Any>): Observable<MainResponse>

    @GET("api/patient/search_doctor")
    suspend fun apiGetDoctorsList(@Query("page") page: Int): MainResponse

    @GET("api/patient/search_doctor")
    suspend fun apiGetDoctorsList(@Query("page") page: Int, @Query("search") search: String): MainResponse

    /*  doctor*/
    @GET("api/patient/doctor_catagory")
    fun apiDoctorsCategories(): Observable<CategoryResponse>

    @GET("api/patient/doctor_catagory/{id}")
    fun apiDoctorByCategories(@Path("id") id: Int?, @QueryMap hashMap: HashMap<String, Any>): Observable<DoctorListResponse>

    @GET("api/patient/doctor_catagory/{id}")
    suspend fun apiDoctorByCategories(@Path("id") id: Int, @Query("page") page: Int): DoctorListResponse

    @GET("api/patient/doctor_catagory/{id}")
    fun apiDoctorFilterByCategories(@Path("id") id: Int?, @QueryMap hashMap: HashMap<String, Any>): Observable<DoctorListResponse>

    /* Appointment*/
    @GET("api/patient/appointment")
    fun apiAppointments(): Observable<AppointmentResponse>

    @GET("api/patient/visited_doctors")
    fun apVisitedDoctors(): Observable<VisitedAppointmentDoc>

    @FormUrlEncoded
    @POST("api/patient/cancel_appointment")
    fun apiCancelAppointment(@FieldMap hashMap: HashMap<String, Any>): Observable<Response>

    /*visted details*/
    @FormUrlEncoded
    @POST("api/patient/feedback")
    fun apiPostFeedback(@FieldMap hashMap: HashMap<String, Any>): Observable<FeedbackResponse>

    /*doctor profile*/
    @FormUrlEncoded
    @POST("api/patient/favourite_doctor")
    fun apiAddFav(@FieldMap hashMap: HashMap<String, Any>): Observable<Response>

    /* TODO Medical Record*/
    @GET("api/patient/records_list")
    fun apiMedicalRecord(): Observable<MedicalRecord>

    @Multipart
    @POST("api/patient/medical_records")
    fun apiAddMedicalRecords(@PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Observable<ResponsePrescription>

    @GET("api/patient/doctor")
    fun apiAllDoctors(): Observable<ResponseDoctors>

    @GET("api/patient/records_details/{record_id}")
    fun apiRecordsList(@Path("record_id") id: String?): Observable<ResponseMedicalDetails>

    @GET("api/patient/articles")
    fun apiArticles(): Observable<ArticleResponse>

    @FormUrlEncoded
    @POST("api/patient/profile")
    fun apiEditPatient(@FieldMap hashMap: HashMap<String, Any>): Observable<ProfileResponse>


    /*TODO RELATIVE*/
    @GET("api/patient/relative/list")
    fun apiRelativeList(@QueryMap hashMap: HashMap<String, Any>): Observable<RelativeResponse>

    @GET("api/patient/relative/{id}")
    fun apiSingleRelative(@Path("id") id: String?): Observable<RelativeResponse>

    @Multipart
    @POST("api/patient/relative")
    fun apiAddPatientRelative(@PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Observable<AddUpdateRelative>

    @FormUrlEncoded
    @POST("api/patient/relative")
    fun apiAddPatientRelative(@FieldMap params: HashMap<String, Any>): Observable<AddUpdateRelative>

    @Multipart
    @POST("api/patient/relative/{id}")
    fun apiUpdateRelativePatient(@Path("id") id: String?, @PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Observable<AddUpdateRelative>

    @FormUrlEncoded
    @POST("api/patient/relative/{id}")
    fun apiUpdateRelativePatient(@Path("id") id: String?, @FieldMap params: HashMap<String, Any>): Observable<AddUpdateRelative>

    @Multipart
    @POST("api/patient/profile")
    fun apiEditPatientWithImage(@PartMap hashMap: HashMap<String, RequestBody>, @Part image: MultipartBody.Part?): Observable<ProfileResponse>

    /*TODO CHAT*/
    @GET("api/patient/chat/history")
    fun apiChat(): Observable<ChatListResponse>

    @GET("api/patient/chat/status/{id}")
    fun apiChatStatus(@Path("id") int: Int): Observable<ChatStatusResponse>

    @GET("api/patient/chat_push")
    fun apiPostChat(@QueryMap hashMap: HashMap<String, String>): Call<Object>

    /*TODO SEARCH*/
    @GET("api/patient/home_search")
    fun apiGlobalSearchApp(@QueryMap hashMap: HashMap<String, Any>): Observable<SearchResponse>

    @FormUrlEncoded
    @POST("api/patient/reminder")
    fun apiAddRemainder(@FieldMap hashMap: HashMap<String, Any>): Observable<AddRemainderResponse>

    @GET("api/patient/reminder_list")
    fun apiReminders(): Observable<ReminderResponse>

    @FormUrlEncoded
    @POST("api/patient/chat/promocode")
    fun apiAddChatPromoCode(@FieldMap hashMap: HashMap<String, Any>): Observable<ChatPromoSuccess>

    @FormUrlEncoded
    @POST("api/patient/payment")
    fun apiPayForChatRequest(@FieldMap hashMap: HashMap<String, Any>): Observable<MessageResponse>


    @GET("api/patient/video/call/check")
    fun apiVideoCheckStatusAPI(): Observable<VideoStatusCheck>

    /* TODO TWILIO CALL */
    @GET("api/patient/video/cancel")
    fun apiCancelVideoCall(@QueryMap hashMap: HashMap<String, Any>): Call<VideoCallCancelResponse>?

    @GET("api/patient/video/call/token")
    fun apiTwilloVideoToken(@QueryMap hashMap: HashMap<String, Any>): Call<AccessToken>

    @GET("api/patient/video/call")
    fun apiCallRequest(@QueryMap hashMap: HashMap<String, Any>): Call<AccessToken>


    /* TODO CARD */
    @GET("api/patient/card?user_type=patient")
    fun apiCards(): Observable<List<CardList>>

    @FormUrlEncoded
    @POST("api/patient/card")
    fun apiAddCardDetails(@FieldMap hashMap: HashMap<String, Any>): Observable<CardSuccessMessage>

    @FormUrlEncoded
    @POST("api/patient/delete/card")
    fun apiDeleteCardDetails(@FieldMap hashMap: HashMap<String, Any>): Observable<CardSuccessMessage>

    @FormUrlEncoded
    @POST("api/patient/add_money")
    fun apiAddMoney(@FieldMap hashMap: HashMap<String, Any>): Observable<WalletAddSuccess>

    @GET("api/patient/faq")
    fun apiFaqList(): Observable<FaqResponse>
}