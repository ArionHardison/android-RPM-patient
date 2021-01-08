
package com.telehealthmanager.app.repositary


import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.pagination.DoctorDataSource
import com.telehealthmanager.app.repositary.pagination.DoctorSearchDataSource
import com.telehealthmanager.app.repositary.pagination.FindDoctorSource
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
            .apiSignUp(params)
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
            .apiSendOtp(params)
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
            .apiVerifyOtp(params)
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
            .apiSignIn(params)
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
            .apiLogOut(params)
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
            .apiGetHome(params)
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
            .apiGetProfile()
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
            .apiUpdatePassword(hashMap)
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
            .apiDoctorsCategories()
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
            .apiBookDoctor(params)
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

    fun getDoctorByCategorys(viewModel: ViewModel, id: Int, hashMap: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .apiDoctorByCategories(id, hashMap)
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
            .apiDoctorFilterByCategories(id, hashMap)
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
    fun getAppointment(viewModel: AppointmentViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .apiAppointments()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponseUpcoming.value = it
                viewModel.mResponsePrevious.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getUpcomingAppointment(viewModel: AppointmentViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .apiAppointments()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponseUpcoming.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getVisitedDoc(viewModel: ViewModel): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .apVisitedDoctors()
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
            .apiCancelAppointment(params)
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
            .apiPostFeedback(params)
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
            .apiAddFav(params)
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
            .apiArticles()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mArticleResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun editPatientApi(
        editPatientViewModel: ProfileViewModel,
        hashMap: HashMap<String, Any>
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .apiEditPatient(hashMap)
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
            .apiRelativeList(hashMap)
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
            .apiAddPatientRelative(hashMap, image)
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
            .apiAddPatientRelative(hashMap)
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
            .apiUpdateRelativePatient(relativeID, hashMap, image)
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
            .apiUpdateRelativePatient(relativeID, hashMap)
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
            .apiSingleRelative(relativeID)
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
            .apiChat()
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
            .apiChatStatus(id)
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
            .apiEditPatientWithImage(hashMap, image)
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
            .apiMedicalRecord()
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
            .apiAllDoctors()
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
            .apiAddMedicalRecords(hashMap, image)
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
            .apiRecordsList(medicalId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponseMedicalDetails.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    /*TODO Search*/
    fun getGlobalSearch(viewModel: SearchGlobalViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .apiGlobalSearchApp(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun addRemainder(addRemainderViewModel: AddReminderViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .apiAddRemainder(params)
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
            .apiReminders()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mReminderResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun addChatPromoCode(chatViewModel: ChatSummaryViewModel, hashMap: java.util.HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .apiAddChatPromoCode(hashMap)
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
            .apiPayForChatRequest(hashMap)
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
            .apiVideoCheckStatusAPI()
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
            .apiCards()
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
            .apiAddCardDetails(hashMap)
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
            .apiDeleteCardDetails(hashMap)
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
            .apiAddMoney(hashMap)
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
            .apiFaqList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mFaqResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }

    fun getResults(default: Int, searchViewModel: SearchViewModel) = Pager(PagingConfig(pageSize = 10),
        pagingSourceFactory = { DoctorDataSource(this, default, searchViewModel) }
    ).liveData

    fun getSearchResults(query: String, searchViewModel: SearchViewModel) = Pager(PagingConfig(pageSize = 10),
        pagingSourceFactory = { DoctorSearchDataSource(this, query, searchViewModel) }
    ).liveData

    fun getFindDoc(query: String, searchViewModel: FindDoctorsViewModel) = Pager(PagingConfig(pageSize = 10),
        pagingSourceFactory = { FindDoctorSource(this,  searchViewModel) }
    ).liveData


}
