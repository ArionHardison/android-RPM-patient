package com.midokter.app.repositary

object WebApiConstants {

    const val SALT_KEY = "salt_key"
    const val REQUESTED_WITH = "X-Requested-With"
    const val HTTP_REQUEST = "XMLHttpRequest"
    const val AUTHORIZATION = "Authorization"

    const val OLD_PASSWORD = "old_password"
    const val NEW_PASSWORD = "password"
    const val CONFIRM_PASSWORD = "password_confirmation"

    object SignIn {
        const val COUNTRY_CODE = "country_code"
        const val MOBILE = "mobile"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val GRANT_TYPE = "grant_type"
        const val CLIENT_ID = "client_id"
        const val CLIENT_SECRET = "client_secret"
        const val OTP = "otp"
    }




    object AddAppointment {
        const val PATIENT_ID="patient_id"
        const val DOCTOR_ID="doctor_id"
        const val SERVICE_ID="service_id"
        const val SCHEDULED_AT="scheduled_at"
        const val CONSULT_TIME="consult_time"
        const val APPOINTMENT_TYPE="appointment_type"
        const val DESCRIPTION="description"
        const val FIRST_NAME="first_name"
        const val LAST_NAME="last_name"
        const val EMAIL="email"
        const val PHONE="phone"
        const val GENDER="gender"
        const val AGE="age"
    }


    object SocialLogin {
        const val DEVICE_TYPE = "device_type"
        const val DEVICE_TOKEN = "device_token"
        const val DEVICE_ID = "device_id"
        const val LOGIN_BY = "login_by"
        const val SOCIAL_UNIQUE_ID = "social_unique_id"
    }

    object ForgotPassword {
        const val ACCOUNT_TYPE = "account_type"
        const val COUNTRY_CODE = "country_code"
        const val MOBILE = "mobile"
        const val EMAIL = "email"
    }



    object ResetPassword {
        const val ACCOUNT_TYPE = "account_type"
        const val COUNTRY_CODE = "country_code"
        const val USERNAME = "username"
        const val OTP = "otp"
        const val PASSWORD = "password"
        const val PASSWORD_CONFIRMATION = "password_confirmation"
    }

    object SignUp {
        const val DEVICE_TYPE = "device_type"
        const val DEVICE_TOKEN = "device_token"
        const val DEVICE_ID = "device_id"
        const val LOGIN_BY = "login_by"
        const val GRANDTYPE = "grant_type"
        const val DOB = "dob"
        const val PASSWORD = "password"
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val GENDER = "gender"
        const val COUNTRY_CODE = "country_code"
        const val MOBILE = "mobile"
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val OTP = "otp"
        const val CONFIRM_PASSWORD = "password_confirmation"
        const val COUNTRY_ID = "country_id"
        const val STATE_ID = "state_id"
        const val CITY_ID = "city_id"
    }

    object ChangePassword {
        const val OLD_PASSWORD = "old_password"
        const val PASSWORD = "password"
        const val CURRENTPASSWORD = "current_password"
        const val PASSWORDCONFIRMATION = "password_confirmation"

    }

    object Home {
        const val FROM_DATE = "from_date"
        const val TO_DATE = "to_date"
        const val SEARCH = "search"
    }

    object AddWallet {
        const val AMOUNT = "amount"
        const val CARD_ID = "card_id"
        const val USER_TYPE = "user_type"
        const val PAYMENT_MODE = "payment_mode"
    }

    object addCard {
        const val STRIP_TOKEN = "stripe_token"
    }

    object profile {
        const val FIRST_NAME = "first_name"
        const val LAST_NAME = "last_name"
        const val SPECIALIZATION = "specialities"
        const val MOBILE = "mobile"
        const val EMAIL = "email"
    }
}