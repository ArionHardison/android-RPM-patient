package com.telehealthmanager.app.repositary


import androidx.lifecycle.ViewModel
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.ui.activity.addmedicalrecord.DoctorMedicalRecordsViewModel
import com.telehealthmanager.app.ui.activity.addmoney.AddMoneyViewModel
import com.telehealthmanager.app.ui.activity.addreminder.AddReminderViewModel
import com.telehealthmanager.app.ui.activity.allergies.AllergiesViewModel
import com.telehealthmanager.app.ui.activity.changepassword.ChangePasswordViewModel
import com.telehealthmanager.app.ui.activity.chat.ChatSummaryViewModel
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.telehealthmanager.app.ui.activity.login.LoginViewModel
import com.telehealthmanager.app.ui.activity.main.MainViewModel
import com.telehealthmanager.app.ui.activity.main.ui.appointment.AppointmentViewModel
import com.telehealthmanager.app.ui.activity.main.ui.articles.ArticleViewModel
import com.telehealthmanager.app.ui.activity.main.ui.faq.FaqViewModel
import com.telehealthmanager.app.ui.activity.main.ui.favourite_doctor.FavouriteDoctorViewModel
import com.telehealthmanager.app.ui.activity.main.ui.medical_records.MedicalRecordsViewModel
import com.telehealthmanager.app.ui.activity.main.ui.online_consultation.OnlineConsultationViewModel
import com.telehealthmanager.app.ui.activity.main.ui.relative_management.RelativeMgmtViewModel
import com.telehealthmanager.app.ui.activity.main.ui.remainder.RemainderViewModel
import com.telehealthmanager.app.ui.activity.main.ui.settings.SettingViewModel
import com.telehealthmanager.app.ui.activity.main.ui.wallet.WalletViewModel
import com.telehealthmanager.app.ui.activity.otp.OTPViewModel
import com.telehealthmanager.app.ui.activity.patientDetail.PatientDetailViewModel
import com.telehealthmanager.app.ui.activity.payment.PaymentViewModel
import com.telehealthmanager.app.ui.activity.profile.ProfileViewModel
import com.telehealthmanager.app.ui.activity.register.RegisterViewModel
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchViewModel
import com.telehealthmanager.app.ui.activity.searchGlobal.SearchGlobalViewModel
import com.telehealthmanager.app.ui.activity.visitedDoctor.VisitedDoctorsViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class AppRepository : BaseRepository() {

    companion object {
        private var appRepository: AppRepository? = null

        fun instance(): AppRepository {
            if (appRepository == null) synchronized(AppRepository) {
                appRepository = AppRepository()
            }
            return appRepository!!
        }
    }


    /*TODO singup*/
    fun signUp(viewModel: RegisterViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .signup(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mRegisterResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun sendOtp(viewModel: LoginViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .Sendotp(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.motpResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun verifiyOtp(viewModel: OTPViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .verifyotp(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.loginResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun postSignIn(viewModel: OTPViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .signIn(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.loginResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun logout(viewModel: SettingViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .logout(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mLogoutResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    /*TODO home*/
    fun getHome(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getHome(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is FavouriteDoctorViewModel) {
                    viewModel.mDoctorResponse.value = it
                } else if (viewModel is SearchViewModel) {
                    viewModel.mDoctorResponse.value = it
                }
            }, {
                if (viewModel is FavouriteDoctorViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                } else if (viewModel is SearchViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    /*TODO profile*/
    fun getProfile(viewModel: ViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getProfile()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is MainViewModel) {
                    viewModel.mProfileResponse.value = it
                } else if (viewModel is ProfileViewModel) {
                    viewModel.mProfileResponse.value = it
                } else if (viewModel is WalletViewModel) {
                    viewModel.mProfileResponse.value = it
                } else if (viewModel is AllergiesViewModel) {
                    viewModel.mProfileResponse.value = it
                }
            }, {
                if (viewModel is MainViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                } else if (viewModel is ProfileViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                } else if (viewModel is WalletViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                } else if (viewModel is AllergiesViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    /*TODO  changePassword*/
    fun changePassword(changePasswordViewModel: ChangePasswordViewModel, hashMap: java.util.HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .updatePassword(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                changePasswordViewModel.mResponse.value = it
            }, {
                changePasswordViewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    /*TODO Category*/
    fun getCategorys(viewModel: ViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getCategorys()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is FindDoctorsViewModel) {
                    viewModel.mCategoryResponse.value = it
                }
            }, {
                if (viewModel is FindDoctorsViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun bookDoctor(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .bookdoctor(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is PatientDetailViewModel) {
                    viewModel.mBookedResponse.value = it
                }
                if (viewModel is FindDoctorsViewModel) {
                    viewModel.mBookedResponse.value = it
                }
            }, {
                if (viewModel is PatientDetailViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
                if (viewModel is FindDoctorsViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun getDoctorByCategorys(viewModel: ViewModel, id: Int): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getDoctorByCategorys(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is FindDoctorsViewModel) {
                    viewModel.mDoctorResponse.value = it
                }
            }, {
                if (viewModel is FindDoctorsViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun getFiltersDoctors(viewModel: ViewModel, id: Int, hashMap: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getDoctorFilterByCategories(id, hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is FindDoctorsViewModel) {
                    viewModel.mDoctorResponse.value = it
                }
            }, {
                if (viewModel is FindDoctorsViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    /*TODO Appointments*/
    fun getAppointment(viewModel: ViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getAppointment()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is AppointmentViewModel) {
                    viewModel.mResponse.value = it
                }
            }, {
                if (viewModel is AppointmentViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun getVisitedDoc(viewModel: ViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getVisitedDoc()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is VisitedDoctorsViewModel) {
                    viewModel.mVisitedAppointmentDoc.value = it
                }
            }, {
                if (viewModel is VisitedDoctorsViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun cancelAppointment(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .cancelAppointment(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is AppointmentViewModel) {
                    viewModel.mCancelResponse.value = it
                }
                if (viewModel is VisitedDoctorsViewModel) {
                    viewModel.mCancelResponse.value = it
                }
            }, {
                if (viewModel is AppointmentViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
                if (viewModel is VisitedDoctorsViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }


    fun postFeedback(viewModel: VisitedDoctorsViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .postfeedback(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mFeedbackResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    /*TODO ADD FAV*/
    fun addFav(viewModel: SearchViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addfav(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mFavResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getArticles(viewModel: ArticleViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getArticles()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is ArticleViewModel) {
                    viewModel.mArticleResponse.value = it
                }
            }, {
                if (viewModel is ArticleViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun editPatientApi(
        editPatientViewModel: ProfileViewModel,
        hashMap: HashMap<String, Any>
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .editPatient(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mEditPatientResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })

    }

    /*TODO Relativies*/
    fun getRelativeLists(
        editPatientViewModel: RelativeMgmtViewModel,
        hashMap: HashMap<String, Any>
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getRelativeList(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mRelativeResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })

    }


    fun addRelativePatientApi(
        editPatientViewModel: ProfileViewModel,
        hashMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addPatientRelative(hashMap, image)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mAddRelativeResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun addRelativePatientApi(
        editPatientViewModel: ProfileViewModel,
        hashMap: HashMap<String, Any>
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addPatientRelative(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mAddRelativeResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })

    }


    fun updateRelativePatientApi(
        editPatientViewModel: ProfileViewModel,
        relativeID: String,
        hashMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .updateRelativePatient(relativeID, hashMap, image)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mAddRelativeResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun updateRelativePatientApi(
        editPatientViewModel: ProfileViewModel,
        relativeID: String,
        hashMap: HashMap<String, Any>
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .updateRelativePatient(relativeID, hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mAddRelativeResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getRelativePatientApi(
        editPatientViewModel: ProfileViewModel,
        relativeID: String
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getSingleRelative(relativeID)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mRelativeResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })

    }


    fun getChatsAPI(
        onlineConsultationViewModel: OnlineConsultationViewModel
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getChat()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                onlineConsultationViewModel.mChatResponse.value = it
            }, {
                onlineConsultationViewModel.loadingProgress.value = false
                onlineConsultationViewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getChatStatusAPI(
        onlineConsultationViewModel: OnlineConsultationViewModel, id: Int
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getChatStatus(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                onlineConsultationViewModel.mChatStatusResponse.value = it
            }, {
                onlineConsultationViewModel.loadingProgress.value = false
                onlineConsultationViewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun editPatientWithImageApi(
        id: Int,
        editPatientViewModel: ProfileViewModel,
        hashMap: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .editPatientWithImage(hashMap, image)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mEditPatientResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })

    }

    /*TODO Medical Records*/
    fun getMedicalRecord(viewModel: MedicalRecordsViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getMedicalRecord()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mMedicalRecordResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getAllDoc(viewModel: DoctorMedicalRecordsViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getAllDoctors()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponseDoctors.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun addMedicalRecord(viewModel: DoctorMedicalRecordsViewModel, hashMap: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addMedicalRecords(hashMap, image)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mAddResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getMedicalRecordsList(viewModel: DoctorMedicalRecordsViewModel, medicalId: String): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getRecordsList(medicalId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponseMedicalDetails.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    /*TODO Search*/
    fun getGloblSearch(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getGlobalSearchApp(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is SearchGlobalViewModel) {
                    viewModel.mResponse.value = it
                }
            }, {
                if (viewModel is SearchGlobalViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }

            })
    }

    fun addRemainder(addRemainderViewModel: AddReminderViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addRemainder(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                addRemainderViewModel.mAddRemainderResponse.value = it
            }, {
                addRemainderViewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }


    fun getReminders(viewModel: RemainderViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getReminders()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is RemainderViewModel) {
                    viewModel.mReminderResponse.value = it
                }
            }, {
                if (viewModel is RemainderViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun addChatPromoCode(chatViewModel: ChatSummaryViewModel, hashMap: java.util.HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addChatPromoCode(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                chatViewModel.mChatPromoResponse.value = it
            }, {
                chatViewModel.getErrorObservable().value = BaseApplication.baseApplication.getString(R.string.invalid_promo_code)
            })
    }

    fun payForChatRequest(viewmodel: PaymentViewModel, hashMap: java.util.HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .payForChatRequest(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewmodel.mChatRequestResponse.value = it
            }, {
                viewmodel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun videoCheckStatusAPI(viewModel: MainViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .videoCheckStatusAPI()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is MainViewModel) {
                    viewModel.mVideoIncomingResponse.value = it
                }
            }, {
                if (viewModel is MainViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    /*TODO CARDS*/
    fun getCardList(viewModel: AddMoneyViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getCards()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mCardListResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun goToAddAddCard(viewModel: AddMoneyViewModel, hashMap: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addCardDetails(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mAddCardSuccess.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun goToDeleteCard(viewModel: AddMoneyViewModel, hashMap: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .deleteCardDetails(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mDeletedSuccess.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun goToAddMoney(viewModel: AddMoneyViewModel, hashMap: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addMoney(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mAddMoneySuccess.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getFaqCall(viewModel: FaqViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getFaqList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mFaqResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

}