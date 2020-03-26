package com.midokter.app.utils

import android.app.ActivityManager
import android.content.Context

object AppUtils {

    fun isAppIsInBackground(context: Context): Boolean {
        var isInBackground = true
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val taskInfo = am.getRunningTasks(1)
        val componentInfo = taskInfo[0].topActivity
        if (componentInfo!!.packageName == context.packageName) {
            isInBackground = false
        }

        return isInBackground
    }

}