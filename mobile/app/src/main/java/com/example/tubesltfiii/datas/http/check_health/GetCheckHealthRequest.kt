package com.example.tubesltfiii.datas.http.check_health

import com.example.tubesltfiii.datas.http.BaseRequest
import com.example.tubesltfiii.utils.DateHelper.toIsoString
import java.util.*

data class GetCheckHealthRequest(
    val limit: Number?,
    val dateStart: Date?,
    val dateEnd: Date?
) : BaseRequest() {
    override fun toJsonString(): String {
        return """
            {
                "limit": ${limit.toString()},
                "dateStart": ${dateStart.toIsoString()},
                "dateEnd": ${dateEnd.toIsoString()}
            }
        """.trimIndent()
    }
}