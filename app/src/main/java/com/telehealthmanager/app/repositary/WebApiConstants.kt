package com.telehealthmanager.app.repositary

object WebApiConstants {

    const val SALT_KEY = "salt_key"
    const val REQUESTED_WITH = "X-Requested-With"
    const val HTTP_REQUEST = "XMLHttpRequest"
    const val AUTHORIZATION = "Authorization"

    const val OLD_PASSWORD = "old_password"
    const val NEW_PASSWORD = "password"


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

    object Wallet {
        const val AMOUNT = "amount"
    }

    object AddRemainder {
        const val NAME = "name"
        const val DATE = "date"
        const val TIME = "time"
        const val ALARM = "alarm"
        const val NOTIFY_ME = "notify_me"
    }


    object IntentPass {
        const val Appointment = "Appointment"
        const val Invoice = "Invoice"
        const val VisitedDoctor = "VisitedDoctor"
        const val DoctorProfile = "DoctorProfile"
        const val FavDoctorProfile = "FavDoctorProfile"
        const val SearchDoctorProfile = "SearchDoctorProfile"
        const val SERVICE_LIST = "serviceList"
        const val iscancel = "iscancel"
        const val MEDICAL_RECORD = "medical_record"
        const val ID="id"
    }

    object Feedback {
        const val appointment_id = "appointment_id"
        const val hospital_id = "hospital_id"
        const val experiences = "experiences"
        const val visited_for="visited_for"
        const val comments="comments"
        const val rating="rating"
        const val title="title"
    }

    object Favourite {
        const val patient_id = "patient_id"
        const val doctor_id = "doctor_id"
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
        const val ID="id"
    }

    object EditPatient {
        const val FIRST_NAME="first_name"
        const val LAST_NAME="last_name"
        const val PHONE="phone"
        const val EMAIL="email"
        const val GENDER="gender"
        const val DOB="dob"
        const val BLOOD_GROUP="blood_group"
        const val MERITAL_STATUS="merital_status"
        const val HEIGHT="height"
        const val WEIGHT="weight"
        const val EMERGENCY_CONTACT="emergency_contact"
        const val LOCATION="location"
        const val ALLERGIES="allergies"
        const val CURRENT_MEDICATIONS="current_medications"
        const val PAST_MEDICATIONS="past_medications"
        const val CHRONIC_DISEASES="chronic_diseases"
        const val INJURIES="injuries"
        const val SURGERIES="surgeries"
        const val SMOKING="smoking"
        const val ALCOHOL="alcohol"
        const val ACTIVITY="activity"
        const val FOOD="food"
        const val OCCUPATION="occupation"
        const val PROFILE_PIC="profile_pic"
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