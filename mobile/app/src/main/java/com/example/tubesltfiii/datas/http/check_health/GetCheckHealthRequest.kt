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
        var dateStartText = "null"
        var dateEndText = "null"
        if(dateStart != null) {
            dateStartText = "\"${dateStart.toIsoString()}\""
        }
        if(dateEnd != null) {
            dateEndText = "\"${dateEnd.toIsoString()}\""
        }
        return """
            {
                "limit": ${limit.toString()},
                "dateStart": $dateStartText,
                "dateEnd": $dateEndText
            }
        """.trimIndent()
    }
}