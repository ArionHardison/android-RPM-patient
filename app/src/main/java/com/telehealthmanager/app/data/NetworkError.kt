package com.telehealthmanager.app.data

object NetworkError {
    const val TIME_OUT = "Network slow. Retry again!"
    const val IO_EXCEPTION = "Internal Server. Retry later"
    const val SERVER_EXCEPTION = "Something went wrong. Please try later"
    const val DATA_EXCEPTION = "Internal Error. Retry later"
    const val BAD_GATE_WAY = "Bad Gateway"
    const val URL_NOT_FOUND = "Url not found"
    const val METHOD_NOT_FOUND = "Method Not Allowed"
    const val BAD_REQUEST = "Bad request"
}