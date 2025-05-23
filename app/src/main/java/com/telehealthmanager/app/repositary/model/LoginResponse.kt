package com.telehealthmanager.doctor.repositary.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class LoginResponse {
    @SerializedName("token")
    @Expose
    private var token: String? = null
    private var error: String? = null
    fun getToken(): String? {
        return token
    }

    fun setToken(token: String?) {
        this.token = token
    }

    fun geterror(): String? {
        return error
    }

    fun seterror(error: String?) {
        this.error = error
    }

}
