package com.example.tubesltfiii.datas.http.check_health

import com.example.tubesltfiii.datas.http.BaseRequest
import com.example.tubesltfiii.utils.DateHelper.toIsoString
import java.util.*

data class CreateCheckHealthRequest(
    val pulse: Number,
    val oxygenSaturation: Number,
    val date: Date
) : BaseRequest() {
    override fun toJsonString(): String {
        return """
            {
                "oxygenSaturation": ${oxygenSaturation.toString()},
                "pulse": ${pulse.toString()},
                "date": "${date.toIsoString()}"
            }
        """.trimIndent()
    }
}
