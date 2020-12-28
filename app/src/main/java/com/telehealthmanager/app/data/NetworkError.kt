package com.telehealthmanager.app.data

object NetworkError {
    const val TIME_OUT = "Network slow. Retry again!"
    const val IO_EXCEPTION = "Internal Server. Retry later"
    const val SERVER_EXCEPTION = "Something went wrong. Please try later"
    const val DATA_EXCEPTION = "Internal Error. Retry later"
}