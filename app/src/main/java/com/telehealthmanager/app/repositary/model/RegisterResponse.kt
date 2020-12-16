package com.telehealthmanager.app.repositary.model

data class RegisterResponse(
    val access_token: AccessToken
) {
    data class AccessToken(
        val exception: Any?,
        val headers: Headers?,
        val original: Original?
    )

    class Headers(

    )

    data class Original(
        val token: String?
    )
}