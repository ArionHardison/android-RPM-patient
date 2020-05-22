package com.midokter.app.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.midokter.app.BaseApplication
import com.midokter.app.R
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.setValue
import com.midokter.app.extensions.observeLiveData
import com.midokter.app.ui.activity.splash.SplashActivity
import com.midokter.app.utils.LocaleUtils
import com.midokter.app.utils.NetworkUtils
import java.util.*

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    protected var baseLiveDataLoading = MutableLiveData<Boolean>()
    private var mViewDataBinding: T? = null
    private var mCustomLoader: CustomLoaderDialog? = null
    val Register_Map: HashMap<String, Any> = HashMap()
    private lateinit var mNoInternetDialog: Dialog

    var languages = arrayOf<CharSequence>("English", "Chinese")
    var defaultLang = 0

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    val loadingObservable: MutableLiveData<*> get() = baseLiveDataLoading

    val isNetworkConnected: Boolean get() = NetworkUtils.isNetworkConnected(applicationContext)

    protected abstract fun initView(mViewDataBinding: ViewDataBinding?)

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initView(mViewDataBinding)

        mCustomLoader = CustomLoaderDialog(this, true)


        try {
            mNoInternetDialog = Dialog(this)
            mNoInternetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mNoInternetDialog.setCancelable(false)
            mNoInternetDialog.setContentView(R.layout.dialog_no_internet)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        observeLiveData(baseLiveDataLoading) {
            if (it!!) showLoading() else hideLoading()
        }

        observeLiveData(BaseApplication.getInternetMonitorLiveData) { isInternetAvailable ->
            if (isInternetAvailable) mNoInternetDialog.dismiss() else mNoInternetDialog.show()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleUtils.setLocale(newBase))
    }

    fun showLoading() {
        if (mCustomLoader != null) {
            Objects.requireNonNull(mCustomLoader!!.window)
                ?.setBackgroundDrawableResource(android.R.color.transparent)
            mCustomLoader!!.show()
        }
    }

    fun hideLoading() {
        if (mCustomLoader != null) mCustomLoader!!.cancel()
    }

    fun showKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun openNewActivity(activity: AppCompatActivity, cls: Class<*>, finishCurrent: Boolean) {
        val intent = Intent(activity, cls)
        startActivity(intent)
        if (finishCurrent) activity.finish()
    }

    protected fun replaceFragment(
        @IdRes id: Int, fragmentName: Fragment,
        fragmentTag: String,
        addToBackStack: Boolean
    ) {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(id, fragmentName, fragmentTag)
        if (addToBackStack)
            transaction.addToBackStack(fragmentTag)
        transaction.commit()
    }

    //Language choosing
    public fun LanguageChooseDialog() {
        AlertDialog.Builder(this!!)
            .setSingleChoiceItems(languages, defaultLang, null)
            .setPositiveButton("Change", DialogInterface.OnClickListener { dialog, whichButton ->
                dialog.dismiss()
                val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                if (selectedPosition == 1) {
                    LocaleUtils.setNewLocale(this!!, "zh")
                    PreferenceHelper(this!!).setValue("language_key", "zh")
                } else {
                    PreferenceHelper(this!!).setValue("language_key", "en")
                    LocaleUtils.setNewLocale(this!!, "en")
                }
                // Do something useful withe the position of the selected radio button
                //Toast.LENGTH_LONG
                val newIntent = Intent(this!!, SplashActivity::class.java)
                newIntent.addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                            Intent.FLAG_ACTIVITY_NEW_TASK
                )
                startActivity(newIntent)
            })
            .show()
    }

}