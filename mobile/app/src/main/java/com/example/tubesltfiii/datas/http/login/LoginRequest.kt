package com.example.tubesltfiii.datas.http.login

import com.example.tubesltfiii.datas.http.BaseRequest

data class LoginRequest(
    val email: String,
    val password: String,
) : BaseRequest() {
    override fun toJsonString(): String {
        return """
            {
                "email": "$email",
                "password": "$password"
            }
        """.trimIndent()
    }
}
