package com.midokter.app.repositary


import androidx.lifecycle.ViewModel
import com.midokter.app.BuildConfig
import com.midokter.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.midokter.app.ui.activity.login.LoginViewModel
import com.midokter.app.ui.activity.main.MainViewModel
import com.midokter.app.ui.activity.main.ui.appointment.AppointmentViewModel
import com.midokter.app.ui.activity.main.ui.favourite_doctor.FavouriteDoctorViewModel
import com.midokter.app.ui.activity.otp.OTPViewModel
import com.midokter.app.ui.activity.profile.ProfileViewModel
import com.midokter.app.ui.activity.register.RegisterViewModel
import com.midokter.app.ui.activity.searchDoctor.SearchViewModel
import com.midokter.app.ui.activity.visitedDoctor.VisitedDoctorsViewModel


import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import retrofit2.http.PartMap

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
                  }
              }, {
                  if (viewModel is MainViewModel) {
                      viewModel.getErrorObservable().value = getErrorMessage(it)
                  }else  if (viewModel is ProfileViewModel) {
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


/*








    fun getPatients(
        viewModel: ViewModel
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getPatients()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is PatientsViewModel) {
                    viewModel.mPatientsResponse.value = it
                }
            }, {
                if (viewModel is PatientsViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun getAppointments(
        viewModel: ViewModel
    ): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .getAppointments("2020-05-16")
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                if (viewModel is AppointmentViewModel) {
                    viewModel.mAppointmentsResponse.value = it
                }
            }, {
                if (viewModel is AppointmentViewModel) {
                    viewModel.getErrorObservable().value = getErrorMessage(it)
                }
            })
    }

    fun addAppointmentApi(addAppointmentViewModel: AddAppointmentViewModel, hashMap: HashMap<String, Any>): Disposable {
        return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
            .addAppointment(hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                addAppointmentViewModel.mAddAppointmentResponse.value = it
            }, {
                addAppointmentViewModel.loadingProgress.value=false
                addAppointmentViewModel.getErrorObservable().value = getErrorMessage(it)
            })
    }*/

}