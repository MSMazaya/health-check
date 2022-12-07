package com.example.tubesltfiii.datas.http.login

import com.example.tubesltfiii.datas.http.BaseResponse

data class LoginResponsePayload(
    val authKey: String
)

data class LoginResponse(
    val payload: LoginResponsePayload?,
    override val status: Number,
    override val message: String
) : BaseResponse