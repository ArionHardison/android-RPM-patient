package com.telehealthmanager.app.utils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.text.format.DateUtils
import android.widget.ArrayAdapter
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

    fun convertRequestBody(data: String): RequestBody {
        return data.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun getTimeAgoFormat(str: String): String {
        val sdf =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        var dateObj: Date? = null
        try {
            dateObj = sdf.parse(str)
            val niceDateStr: String = DateUtils.getRelativeTimeSpanString(
                dateObj.time,
                Calendar.getInstance().timeInMillis,
                DateUtils.MINUTE_IN_MILLIS
            ) as String
            return niceDateStr
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

    @MainThread
    fun selectGender(context: Context, title: String, callBack: ViewCallBack.IItemClick) {
        val types: Array<String> = arrayOf("MALE", "FEMALE")
        val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setIcon(R.drawable.app_logo)
        val adapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_list_item_1, types)
        builder.setAdapter(adapter) { dialog, which ->
            callBack.alertItemClick(adapter.getItem(which).toString())
            dialog.dismiss()
        }
        builder.show()
    }

    @MainThread
    fun selectMarital(context: Context, title: String, callBack: ViewCallBack.IItemClick) {
        val types: Array<String> = arrayOf("Single", "Married", "Others")
        val builder: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setIcon(R.drawable.app_logo)
        val adapter: ArrayAdapter<String> = ArrayAdapter(context, android.R.layout.simple_list_item_1, types)
        builder.setAdapter(adapter) { dialog, which ->
            callBack.alertItemClick(adapter.getItem(which).toString())
            dialog.dismiss()
        }
        builder.show()
    }

    /*fun getTimeAgoFormat(str: String): String {

        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val past = format.parse(str)
        val now = Date()
        val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(now.time - past!!.time)
        val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(now.time - past.time)
        val hours: Long = TimeUnit.MILLISECONDS.toHours(now.time - past.time)
        val days: Long = TimeUnit.MILLISECONDS.toDays(now.time - past.time)

        val difference: Long = abs(now.time - past.time)

        return when {
            difference > DateUtils.WEEK_IN_MILLIS -> {
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
*/

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
            .error(R.drawable.leader_board)
            .placeholder(R.drawable.leader_board)
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
        val dateObj: Date?
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
            val fmtOutFull = SimpleDateFormat("d MMM yyyy, EEE", Locale.getDefault())
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

    fun getCurrentDate(format: String): String {
        val today = Date()
        val sdf = SimpleDateFormat(format, Locale.ENGLISH)
        return sdf.format(today)
    }

    fun getCurrentDateTime(): String {
        val today = Date()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        return sdf.format(today)
    }

    fun getRailWaytoNormal(dateRailWay: String): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        val sdf1 = SimpleDateFormat("hh:mm a", Locale.ENGLISH)
        val date = sdf.parse(dateRailWay)
        return sdf1.format(date!!)
    }

    fun getTime(date: Date): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.ENGLISH)
        return sdf.format(date)
    }

    fun getDateFormat(str: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val date = sdf.format(str)
            return date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }

}
