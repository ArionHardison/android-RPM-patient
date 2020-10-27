package com.telehealthmanager.app.repositary

interface ApiListener {
    fun onSuccess(successData: Any)
    fun onError(error: Throwable)
}