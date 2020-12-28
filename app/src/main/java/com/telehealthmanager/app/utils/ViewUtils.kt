package com.telehealthmanager.app.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
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
import java.util.concurrent.TimeUnit

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
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val past = format.parse(str)
        val now = Date()
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - past!!.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
        val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - past.time)
        return when {
            seconds < 60 -> {
                "$seconds seconds ago"
            }
            minutes < 60 -> {
                "$days minutes ago"
            }
            hours < 24 -> {
                "$hours hours ago"
            }
            else -> {
                "$days days ago"
            }
        }
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

    fun setImageViewGlide(context: Context, imageView: ImageView, imagePath: String) {
        Glide.with(context)
            .load(imagePath)
            .thumbnail(0.5f)
            .error(R.drawable.app_logo)
            .placeholder(R.drawable.app_logo)
            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imageView)
    }

    fun setDocViewGlide(context: Context, imageView: ImageView, imagePath: String) {
        Glide.with(context)
            .load(imagePath)
            .thumbnail(0.5f)
            .error(R.drawable.doc_place_holder)
            .placeholder(R.drawable.doc_place_holder)
            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            .into(imageView)
    }

    fun setMapViewGlide(context: Context, imageView: ImageView, imagePath: String) {
        Glide.with(context)
            .load(imagePath)
            .thumbnail(0.5f)
            .centerCrop()
            .error(R.drawable.shimmer_bg)
            .placeholder(R.drawable.shimmer_bg)
            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
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


    fun getChatTimeFormat(str: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var dateObj: Date? = null
        try {
            dateObj = sdf.parse(str)
            val time = SimpleDateFormat("h:mm a", Locale.getDefault()).format(dateObj)
            val fmtOutFull = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return fmtOutFull.format(dateObj!!.time) + "\n" + time
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

    fun getCurrentDateTime(): String {
        val today = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return sdf.format(today)
    }

}
