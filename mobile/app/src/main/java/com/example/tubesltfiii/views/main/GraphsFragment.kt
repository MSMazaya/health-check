package com.example.tubesltfiii.views.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.tubesltfiii.R
import com.example.tubesltfiii.databinding.FragmentMainGraphsBinding
import com.example.tubesltfiii.databinding.FragmentMainHomeBinding
import com.example.tubesltfiii.datas.CheckHistory
import com.example.tubesltfiii.datas.constants.AUTH_SHARED_PREFERENCES_NAME
import com.example.tubesltfiii.datas.http.check_health.CheckHealthPayload
import com.example.tubesltfiii.datas.http.check_health.CheckHealthResponse
import com.example.tubesltfiii.datas.http.check_health.GetCheckHealthRequest
import com.example.tubesltfiii.views.bluetooth.CheckHistoryCardAdapter
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.Result
import java.sql.Date
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GraphsFragment : Fragment() {

    private var _binding: FragmentMainGraphsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    fun updateChart(chart: String, data: List<CheckHealthPayload>?) {


        when(chart) {
            "SPO2" -> {
                var liveChartModel : AAChartModel = AAChartModel()
                    .chartType(AAChartType.Area)
                    .yAxisTitle("SpO2")
                    .title("Oxygen Saturation")
                    .backgroundColor("#FFFFFF")
                    .series(arrayOf())

                val chartData = mutableListOf<Double>()
                data?.forEach {
                    chartData.add(it.oxygenSaturation.toDouble())
                }
                Log.d("UpdateData", chartData.toString())
                val pulseSeries = AASeriesElement()
                    .color("#40B7FF")
                    .name("SpO2")
                    .data(chartData.toTypedArray());
                binding.chartHydrationLevel.aa_refreshChartWithChartModel(liveChartModel.series(
                    arrayOf(pulseSeries)))
            }
            "PULSE" -> {
                var liveChartModel : AAChartModel = AAChartModel()
                    .chartType(AAChartType.Area)
                    .yAxisTitle("BPM")
                    .title("Pulse")
                    .backgroundColor("#FFFFFF")
                    .series(arrayOf())

                val chartData = mutableListOf<Double>()
                data?.forEach {
                    chartData.add(it.pulse.toDouble())
                }
                Log.d("UpdateData", chartData.toString())

                val pulseSeries = AASeriesElement()
                    .color("#40B7FF")
                    .name("Pulse")
                    .data(chartData.toTypedArray());
                binding.chartPulse.aa_refreshChartWithChartModel(liveChartModel.series(
                    arrayOf(pulseSeries)))
            }
        }
    }

    fun getAndUpdateData(request: GetCheckHealthRequest, chartType: String) {
        val authSharedPreferences = this.activity?.getSharedPreferences(
            AUTH_SHARED_PREFERENCES_NAME,
            AppCompatActivity.MODE_PRIVATE
        )

        var authKey = authSharedPreferences?.getString("authKey", "")

        val httpAsync = "https://mazaya-health-check.deno.dev/get-check-data"
            .httpPost()
            .jsonBody(request.toJsonString())
            .authentication()
            .bearer(authKey.toString())
            .responseObject<CheckHealthResponse> { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.d("ResponseResult", ex.toString())
                    }
                    is Result.Success -> {
                        val data = result.get()

                        Log.d("ResponseResult", data.payload.toString())

                        updateChart(chartType, data.payload)
                    }
                }
            }

        httpAsync.join()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainGraphsBinding.inflate(inflater, container, false)

        val hydrationChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .yAxisTitle("SpO2")
            .title("Oxygen Saturation")
            .backgroundColor("#FFFFFF")
            .series(arrayOf(
                AASeriesElement()
                    .color("#40B7FF")
                    .name("SpO2")
                    .data(arrayOf()),
            )
            )

        binding.chartHydrationLevel.aa_drawChartWithChartModel(hydrationChartModel)

        val pulseChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .yAxisTitle("BPM")
            .title("Pulse")
            .backgroundColor("#FFFFFF")
            .series(arrayOf(
                AASeriesElement()
                    .color("#40B7FF")
                    .name("Pulse")
                    .data(arrayOf()),
            )
            )

        binding.chartPulse.aa_drawChartWithChartModel(pulseChartModel)

        getAndUpdateData(GetCheckHealthRequest(
            null,
            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
            Date()
        ), "SPO2")

        getAndUpdateData(GetCheckHealthRequest(
            null,
            Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
            Date()
        ), "PULSE")

        binding.buttonGroupOxygenSaturation.setOnPositionChangedListener {
            when(it) {
                0 -> getAndUpdateData(GetCheckHealthRequest(
                    null,
                    Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                    Date()
                ), "SPO2")
                1 -> getAndUpdateData(GetCheckHealthRequest(
                    null,
                    Date.from(LocalDate.now().minusDays(7).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                    Date()
                ), "SPO2")
                2 -> getAndUpdateData(GetCheckHealthRequest(
                    null,
                    Date.from(LocalDate.now().minusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                    Date()
                ), "SPO2")
            }
        }

        binding.buttonGroupPulse.setOnPositionChangedListener {
            when(it) {
                0 -> getAndUpdateData(GetCheckHealthRequest(
                    null,
                    Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                    Date()
                ), "PULSE")
                1 -> getAndUpdateData(GetCheckHealthRequest(
                    null,
                    Date.from(LocalDate.now().minusDays(7).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                    Date()
                ), "PULSE")
                2 -> getAndUpdateData(GetCheckHealthRequest(
                    null,
                    Date.from(LocalDate.now().minusMonths(1).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                    Date()
                ), "PULSE")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}