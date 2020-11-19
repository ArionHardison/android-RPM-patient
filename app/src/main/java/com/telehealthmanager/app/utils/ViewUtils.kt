package com.telehealthmanager.app.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.text.format.DateUtils
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.telehealthmanager.app.R
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import permissions.dispatcher.PermissionRequest
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object ViewUtils {

    @MainThread
    fun showToast(context: Context, @StringRes messageResId: Int, isSuccess: Boolean) {
        if (isSuccess)
            Toasty.success(context, messageResId, Toast.LENGTH_SHORT).show()
        else
            Toasty.error(context, messageResId, Toast.LENGTH_SHORT).show()
    }

    @MainThread
    fun showToast(context: Context, messageResId: String?, isSuccess: Boolean) {
        if (isSuccess)
            Toasty.success(context, messageResId!!, Toast.LENGTH_SHORT).show()
        else
            Toasty.error(context, messageResId!!, Toast.LENGTH_SHORT).show()
    }

    @MainThread
    fun showNormalToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    @MainThread
    fun showTransportAlert(
        context: Context, @StringRes messageResId: Int,
        callBack: ViewCallBack.Alert
    ) {
        AlertDialog.Builder(context, R.style.AppTheme)
            .setTitle(R.string.app_name)
            .setMessage(messageResId)
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                callBack.onPositiveButtonClick(
                    dialog
                )
            }
            .setNegativeButton(android.R.string.no) { dialog, which ->
                callBack.onNegativeButtonClick(
                    dialog
                )
            }
            .show()
    }

    fun convertRequestBody(data: String): RequestBody {
        return data.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun showAlert(context: Context, @StringRes messageResId: Int, callBack: ViewCallBack.Alert) {
        AlertDialog.Builder(context)
            .setTitle(R.string.app_name)
            .setMessage(messageResId)
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                callBack.onPositiveButtonClick(
                    dialog
                )
            }
            .setNegativeButton(android.R.string.no) { dialog, which ->
                callBack.onNegativeButtonClick(
                    dialog
                )
            }
            .show()
    }

    @MainThread
    fun showAlert(context: Context, @StringRes messageResId: Int) {
        AlertDialog.Builder(context)
            .setTitle(R.string.app_name)
            .setMessage(messageResId)
            .setPositiveButton(android.R.string.yes) { dialog, which -> dialog.dismiss() }
            .show()
    }

    @MainThread
    fun showRationaleAlert(
        context: Context, @StringRes messageResId: Int,
        request: PermissionRequest
    ) {
        AlertDialog.Builder(context)
            .setTitle(R.string.app_name)
            .setMessage(messageResId)
            .setPositiveButton("Allow") { dialog, which -> request.proceed() }
            .setNegativeButton("Diney") { dialog, which -> request.cancel() }
            .show()
    }

    fun getTimeAgoFormat(str: String): String {
        val sdf =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var dateObj: Date? = null
        try {
            dateObj = sdf.parse(str)
            val niceDateStr: String = DateUtils.getRelativeTimeSpanString(dateObj.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.MINUTE_IN_MILLIS) as String
            return niceDateStr
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }


    fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    fun convertToBitmap(drawable: Drawable, widthPixels: Int, heightPixels: Int): Bitmap {
        val mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(mutableBitmap)
        drawable.setBounds(0, 0, widthPixels, heightPixels)
        drawable.draw(canvas)

        return mutableBitmap
    }

    fun getTimeDifference(date: String): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")

        try {
            val date1 = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().time))
            val date2 = simpleDateFormat.parse(date)

            var different = date2.time - date1.time
            val secondsInMilli: Long = 1000
            val minutesInMilli = secondsInMilli * 60
            val hoursInMilli = minutesInMilli * 60
            val daysInMilli = hoursInMilli * 24

            val elapsedDays = different / daysInMilli
            different = different % daysInMilli

            val elapsedHours = different / hoursInMilli
            different = different % hoursInMilli

            val elapsedMinutes = different / minutesInMilli
            different = different % minutesInMilli

            val elapsedSeconds = different / secondsInMilli

            return if (elapsedHours == 0L)
                if (elapsedMinutes > 1) "$elapsedMinutes mins" else elapsedMinutes.toString() + "min"
            else
                if (elapsedHours > 1) "$elapsedHours hrs" else elapsedMinutes.toString() + "hr"

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return "0"
    }

    fun setImageViewGlide(context: Context, imageView: ImageView, imagePath: String) {

        Glide.with(context)
            .load(imagePath)
            .thumbnail(0.5f)
            .error(R.drawable.app_logo)
            .placeholder(R.drawable.app_logo)
            .into(imageView)

    }

    fun getDayFormat(str: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd ", Locale.ENGLISH)
        val date = formatter.parse(str);
        val format = SimpleDateFormat("dd MMM", Locale.ENGLISH)
        return format.format(date)
    }

    fun getDisplayDayFormat(str: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var dateObj: Date? = null
        try {
            dateObj = formatter.parse(str)
            val fmtOutFull = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
            return String.format("%s", fmtOutFull.format(dateObj!!.time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getDisplayTimeFormat(str: String): String {
        val formatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        var dateObj: Date? = null
        try {
            dateObj = formatter.parse(str)
            val fmtOutFull =
                SimpleDateFormat("h:mm a", Locale.getDefault())
            return String.format("%s", fmtOutFull.format(dateObj!!.time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }


    fun getDayAndTimeFormat(str: String): String {
        val sdf =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var dateObj: Date? = null
        try {
            dateObj = sdf.parse(str)
            val time =
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(dateObj)
            val fmtOutFull =
                SimpleDateFormat("d MMM yyyy, EEE", Locale.getDefault())
            return String.format("%s %s", fmtOutFull.format(dateObj!!.time), time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getScheduleDayFormat(str: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var dateObj: Date? = null
        try {
            dateObj = sdf.parse(str)
            val fmtOutFull =
                SimpleDateFormat("dd MMM yyyy, EE h:mm a", Locale.getDefault())
            return String.format("%s", fmtOutFull.format(dateObj!!.time))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getTimeFormat(str: String): String {
        val sdf =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var dateObj: Date? = null
        try {
            dateObj = sdf.parse(str)
            val time =
                SimpleDateFormat("h:mm a", Locale.getDefault()).format(dateObj)
            return time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    fun getCurrentDate(): String {
        val today = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return sdf.format(today)
    }

    fun getNextDays(dateCount:Int): String {
        val currentCal = Calendar.getInstance()
        currentCal.add(Calendar.DATE, dateCount)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        return sdf.format(currentCal.time)
    }

}
