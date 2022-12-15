package com.example.tubesltfiii.datas.http.check_health

import com.example.tubesltfiii.datas.http.BaseResponse

data class CreateCheckHealthResponse(
    override val message: String,
    override val status: Number
) : BaseResponse