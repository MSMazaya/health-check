package com.example.tubesltfiii.datas.http.check_health

import com.example.tubesltfiii.datas.http.BaseResponse
import java.util.*

data class CheckHealthResponse (
    override val status: Number,
    override val message: String,
    val payload: List<CheckHealthPayload>?
): BaseResponse

data class CheckHealthPayload(
    val _id: String,
    val oxygenSaturation: Number,
    val pulse: Number,
    val date: String,
    val userId: String,
)