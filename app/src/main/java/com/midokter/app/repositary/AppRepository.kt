package com.midokter.app.repositary


import android.content.Context
import androidx.lifecycle.ViewModel
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.ui.activity.addreminder.AddReminderViewModel
import com.midokter.app.ui.activity.chat.ChatSummaryViewModel
import com.midokter.app.ui.activity.chat.ChatViewModel
import com.midokter.app.ui.activity.searchGlobal.SearchGlobalViewModel
import com.midokter.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.midokter.app.ui.activity.login.LoginViewModel
import com.midokter.app.ui.activity.main.MainViewModel
import com.midokter.app.ui.activity.main.ui.appointment.AppointmentViewModel
import com.midokter.app.ui.activity.main.ui.favourite_doctor.FavouriteDoctorViewModel
import com.midokter.app.ui.activity.main.ui.medical_records.MedicalRecordsViewModel
import com.midokter.app.ui.activity.otp.OTPViewModel
import com.midokter.app.ui.activity.patientDetail.PatientDetailViewModel
import com.midokter.app.ui.activity.profile.ProfileViewModel
import com.midokter.app.ui.activity.register.RegisterViewModel
import com.midokter.app.ui.activity.searchDoctor.SearchViewModel
import com.midokter.app.ui.activity.visitedDoctor.VisitedDoctorsViewModel
import com.midokter.app.ui.activity.main.ui.articles.ArticleViewModel
import com.midokter.app.ui.activity.main.ui.remainder.RemainderViewModel
import com.midokter.app.ui.activity.main.ui.wallet.WalletViewModel


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part

class AppRepository : BaseRepository() {

   /* singup*/
    fun signup(viewModel: RegisterViewModel, params: HashMap<String, Any>): Disposable {
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

    fun Sendotp(viewModel: LoginViewModel, params: HashMap<String, Any>): Disposable {
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

    fun verfiyotp(viewModel: OTPViewModel, params: HashMap<String, Any>): Disposable {
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

    fun logout(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .logout(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({

            }, {

            })
    }
/*home*/
      fun getHome(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
          return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
              .getHome(params)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
              .subscribe({
                  if (viewModel is FavouriteDoctorViewModel) {
                      viewModel.mDoctorResponse.value = it
                  }
                 else if (viewModel is SearchViewModel) {
                      viewModel.mDoctorResponse.value = it
                  }
                 else if (viewModel is VisitedDoctorsViewModel) {
                      viewModel.mDoctorResponse.value = it
                  }
              }, {
                  if (viewModel is FavouriteDoctorViewModel) {
                      viewModel.getErrorObservable().value = getErrorMessage(it)
                  }
                  else if (viewModel is SearchViewModel) {
                      viewModel.getErrorObservable().value = getErrorMessage(it)
                  }
                  else if (viewModel is VisitedDoctorsViewModel) {
                      viewModel.getErrorObservable().value = getErrorMessage(it)
                  }
              })
      }
  /*profile*/
     fun getProfile(viewModel: ViewModel): Disposable {
          return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
              .getProfile()
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
              .subscribe({
                  if (viewModel is MainViewModel) {
                      viewModel.mProfileResponse.value = it
                  }else  if (viewModel is ProfileViewModel) {
                      viewModel.mProfileResponse.value = it
                  }else  if (viewModel is WalletViewModel) {
                      viewModel.mProfileResponse.value = it
                  }
              }, {
                  if (viewModel is MainViewModel) {
                      viewModel.getErrorObservable().value = getErrorMessage(it)
                  }else  if (viewModel is ProfileViewModel) {
                      viewModel.getErrorObservable().value = getErrorMessage(it)
                  }else  if (viewModel is WalletViewModel) {
                      viewModel.getErrorObservable().value = getErrorMessage(it)
                  }
              })
      }

   /* Category*/
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

    fun bookdoctor(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
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

    fun getDoctorByCategorys(viewModel: ViewModel,id: Int): Disposable {
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
/*Appointments*/
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

    fun addMoneyToWallet(viewModel: WalletViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addMoneyToWallet(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mWalletResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }


    fun postfeedback(viewModel: VisitedDoctorsViewModel, params: HashMap<String, Any>): Disposable {
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

   /*doctorprofile*/
    fun addfav(viewModel: SearchViewModel, params: HashMap<String, Any>): Disposable {
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
    /*   *//*   updateprofile*//*
    fun postUpdateProfile(viewModel: EditProfileViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .updateprofile(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }
    fun profileUpdate(viewModel: EditProfileViewModel, @PartMap params: HashMap<String, RequestBody>, @Part image: MultipartBody.Part): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .profileUpdate(params, image)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponse.value = it

            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })


    }

    *//*changepassword*//*

    fun changepassword(viewModel: ChangePasswordViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .changepassword(params)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                viewModel.mResponse.value = it
            }, {
                viewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }*/


    companion object {
        private var appRepository: AppRepository? = null

        fun instance(): AppRepository {
            if (appRepository == null) synchronized(AppRepository)  {
                appRepository = AppRepository()
            }
            return appRepository!!
        }
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
        id: Int,
        editPatientViewModel: ProfileViewModel,
        hashMap: HashMap<String, Any>
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .editPatient(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                editPatientViewModel.mEditpatientResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
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
                editPatientViewModel.mEditpatientResponse.value = it
            }, {
                editPatientViewModel.getErrorObservable().value = getErrorMessage(it)
            })

    }

    /*doctorprofile*/
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

    /*search*/
    fun getgloblsearch(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getglobalsearch(params)
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




}