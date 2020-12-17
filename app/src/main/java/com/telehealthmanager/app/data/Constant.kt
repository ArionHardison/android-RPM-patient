@file:Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.telehealthmanager.app.data

import android.os.Build
import android.util.Base64
import androidx.lifecycle.MutableLiveData
import com.telehealthmanager.app.BaseApplication
import java.nio.charset.Charset

object Constant {
    const val M_TOKEN = "Bearer "

    const val CATEGORY_REQUEST_CODE = 210
    const val RELATIVE_ADD_UPDATED = 310
    const val ADD_MEDICAL_RECORD = 310
    const val REQUEST_AUTOCOMPLETE = 201
    const val REQUEST_CODE_ADD_CARD = 100
    const val REQUEST_CODE_ADD_MONEY = 101
    const val PAYMENT_REQUEST_CODE = 102
    const val CUSTOM_PREFERENCE: String = "BaseConfigSetting"
    const val storetype = "Restaurant"
    const val CHAT = "/chat"
    var currency = "$"

    object ModuleTypes {
        val TRANSPORT = "TRANSPORT"
        val ORDER = "ORDER"
        val SERVICE = "SERVICE"
    }

    object Gender {
        val MALE = "MALE"
        val FEMALE = "FEMALE"
        val OTHER = "OTHER"
    }

    object PaymentMode {
        val CASH = "cash"
        val CARD = "card"
    }

    object RequestCode {
        val ADDCARD = 125
        val SELECTED_CARD = 1004
    }

    object HistoryDisputeAPIType {
        val TRANSPORT = "ride"
        val SERVICES = "services"
        val ORDER = "order"
    }

    object MapConfig {
        val DEFAULT_ZOOM = 15.0f
//        val DEFAULT_LOCATION = LatLng(-33.8523341, 151.2106085)
    }

    var isSocketFailed: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { postValue(false) }

    object RoomConstants {
        @JvmField
        var COMPANY_ID: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            String(Base64.decode("", Base64.DEFAULT), Charset.defaultCharset())
        } else {
            TODO("VERSION.SDK_INT < FROYO")
        }
        var CITY_ID: Any? =
            PreferenceHelper(BaseApplication.baseApplication).getValue(PreferenceKey.CITY_ID, 0)
        var TRANSPORT_REQ_ID: Any? = PreferenceHelper(BaseApplication.baseApplication).getValue(
            PreferenceKey.TRANSPORT_REQ_ID,
            0
        )
        var SERVICE_REQ_ID: Any? = PreferenceHelper(BaseApplication.baseApplication).getValue(
            PreferenceKey.SERVICE_REQ_ID,
            0
        )
        var ORDER_REQ_ID: Any? = PreferenceHelper(BaseApplication.baseApplication).getValue(
            PreferenceKey.ORDER_REQ_ID,
            0
        )
    }

    object ROOM_NAME {
        var COMMON_ROOM_NAME: String = "joinCommonRoom"
        var STATUS: String = "socketStatus"
        var NEW_REQ: String = "newRequest"
        var RIDE_REQ: String = "rideRequest"
        var SERVICE_REQ: String = "serveRequest"
        var ORDER_REQ: String = "orderRequest"
        var TRANSPORT_ROOM_NAME: String = "joinPrivateRoom"
        var SERVICE_ROOM_NAME: String = "joinPrivateRoom"
        var ORDER_ROOM_NAME: String = "joinPrivateRoom"
        var JOIN_ROOM_NAME: String = "joinPrivateChatRoom"
        var CHATROOM: String = "send_message"
        var ON_MESSAGE_RECEIVE: String = "new_message"
    }

    object ROOM_ID {
        @JvmField
        var COMMON_ROOM: String =
            "room_${Constant.RoomConstants.COMPANY_ID}_${Constant.RoomConstants.CITY_ID}"
        var TRANSPORT_ROOM: String =
            "room_${Constant.RoomConstants.COMPANY_ID}_${Constant.RoomConstants.TRANSPORT_REQ_ID}_TRANSPORT"
        var SERVICE_ROOM: String =
            "room_${Constant.RoomConstants.COMPANY_ID}_${Constant.RoomConstants.SERVICE_REQ_ID}_SERVICE"
        var ORDER_ROOM: String =
            "room_${Constant.RoomConstants.COMPANY_ID}_${Constant.RoomConstants.ORDER_REQ_ID}_ORDER"
    }

    object BaseUrl {
        @JvmField
        var APP_BASE_URL: String? =
            BaseApplication.getCustomPreference!!.getString(PreferenceKey.BASE_URL, "")
        var TAXI_BASE_URL: String? =
            BaseApplication.getCustomPreference!!.getString(PreferenceKey.TRANSPORT_URL, "")
        var ORDER_BASE_URL: String? =
            BaseApplication.getCustomPreference!!.getString(PreferenceKey.ORDER_URL, "")
        var SERVICE_BASE_URL: String? =
            BaseApplication.getCustomPreference!!.getString(PreferenceKey.SERVICE_URL, "")
    }


    object Chat {
        const val USER_ID = "USER_ID"
        const val REQUEST_ID = "REQUEST_ID"
        const val PROVIDER_ID = "PROVIDER_ID"
        const val USER_NAME = "USER_NAME"
        const val ADMIN_SERVICE = "ADMIN_SERVICE"
        const val PROVIDER_NAME = "PROVIDER_NAME"
    }

    object IntentData {
        val MOBILE_NUMBER = "MOBILE_NUMBER"
        val COUNTRY_CODE = "COUNTRY_CODE"
        val OTP = "OTP"
        val ISLOGIN = "ISLOGIN"
        val IS_VIEW_TYPE = "VIEW_TYPE"
        val IS_RELATIVE_ID = "RELATIVE_ID"
        val WALLET_AMOUNT = "WALLET_AMOUNT"
    }


    object ChangePassword {
        const val OLD_PASSWORD = "old_password"
        const val PASSWORD = "password"
        const val CURRENT_PASSWORD = "current_password"
        const val PASSWORD_CONFIRMATION = "password_confirmation"
    }

    object BookingStatus {
        const val CONSULTED = "CONSULTED"
        const val CANCELLED = "CANCELLED"
    }
}