package com.telehealthmanager.app.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.databinding.DataBindingUtil
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.CustomDialogBinding

class CustomLoaderDialog(context: Context) : Dialog(context) {

    private lateinit var mCustomDialogBinding: CustomDialogBinding
    private var enableLottie: Boolean = false

    constructor(context: Context, enableLottie: Boolean = false) : this(context) {
        this.enableLottie = enableLottie
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        mCustomDialogBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.custom_dialog,
            null,
            false
        )
        setContentView(mCustomDialogBinding.root)
        setCancelable(false)


        /*  if (enableLottie) {
              mCustomDialogBinding.lottieIndicator.visibility = View.VISIBLE
              mCustomDialogBinding.avIndicator.visibility = View.GONE
          } else {
              mCustomDialogBinding.lottieIndicator.visibility = View.GONE
              mCustomDialogBinding.avIndicator.visibility = View.VISIBLE
              mCustomDialogBinding.avIndicator.show()
          }*/

    }

}

