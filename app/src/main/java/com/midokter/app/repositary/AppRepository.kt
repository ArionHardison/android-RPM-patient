package com.midokter.app.repositary


import androidx.lifecycle.ViewModel
import com.midokter.app.BuildConfig
import com.midokter.app.ui.activity.login.LoginViewModel
import com.midokter.app.ui.activity.main.MainViewModel
import com.midokter.app.ui.activity.otp.OTPViewModel
import com.midokter.app.ui.activity.profile.ProfileViewModel
import com.midokter.app.ui.activity.register.RegisterViewModel


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
    /*  fun getHome(viewModel: ViewModel, params: HashMap<String, Any>): Disposable {
          return BaseRepository().createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java)
              .getHome(params)
              .observeOn(AndroidSchedulers.mainThread())
              .subscribeOn(Schedulers.io())
              .subscribe({
                  if (viewModel is HomeViewModel) {
                      viewModel.mhomeResponse.value = it
                  }
              }, {
                  if (viewModel is HomeViewModel) {
                      viewModel.getErrorObservable().value = getErrorMessage(it)
                  }
              })
      }*/
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