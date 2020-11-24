package com.telehealthmanager.app.ui.twilio

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.telehealthmanager.app.fcm.ReceiverPushResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CallDeclineReceiver : BroadcastReceiver() {
    companion object{
        private val TAG =CallDeclineReceiver::class.java.simpleName
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "onReceive: ")
        if (intent?.action == CallReceiveService.CALL_CANCEL_ACTION) {
            context?.stopService(Intent(context, CallReceiveService::class.java))
            val data: ReceiverPushResponse = intent.getParcelableExtra(TwilloVideoActivity.RECEIVE_PUSH_INFO)!!

            /*CoroutineScope(Dispatchers.IO).launch {
                try {
                    RetrofitClient.apiService.updateCallStatus(mRequest)
                }catch (e:Exception){
                    Log.e(TAG, "onReceive: ${e.message}" )
                }
            }*/
        }
    }
}