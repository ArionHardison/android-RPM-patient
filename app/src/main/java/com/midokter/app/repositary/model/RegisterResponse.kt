package com.midokter.app.repositary.model

data class RegisterResponse(
    val access_token: AccessToken,
    val created_at: String,
    val email: String,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val phone: String,
    val token_type: String,
    val updated_at: String
) {
    data class AccessToken(
        val exception: Any,
        val headers: Headers,
        val original: Original
    ) {
        class Headers(
        )

        data class Original(
            val token: String
        )
    }
}