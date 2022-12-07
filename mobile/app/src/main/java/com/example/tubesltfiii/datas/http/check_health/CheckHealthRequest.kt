package com.example.tubesltfiii.datas.http.check_health

import com.example.tubesltfiii.datas.http.BaseRequest
import java.util.*

data class CheckHealthRequest(
    val limit: Number?,
    val dateStart: Date?,
    val dateEnd: Date?
) : BaseRequest() {
    override fun toJsonString(): String {
        return """
            {
                "limit": ${limit.toString()},
                "dateStart": ${dateStart.toString()},
                "dateEnd": ${dateEnd.toString()}
            }
        """.trimIndent()
    }
}