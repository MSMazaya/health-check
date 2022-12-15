package com.example.tubesltfiii.views.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.airbnb.lottie.LottieAnimationView
import com.example.tubesltfiii.databinding.FragmentMainAddDataBinding
import com.example.tubesltfiii.datas.constants.AUTH_SHARED_PREFERENCES_NAME
import com.example.tubesltfiii.datas.http.check_health.CreateCheckHealthResponse
import com.example.tubesltfiii.datas.http.check_health.CreateCheckHealthRequest
import com.example.tubesltfiii.utils.DateHelper.toIsoString
import com.example.tubesltfiii.views.main.viewmodel.MainViewModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.Result
import kotlinx.coroutines.*
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddDataFragment : Fragment(), CoroutineScope by MainScope() {

    private var _binding: FragmentMainAddDataBinding? = null
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()
    private var job: Job? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var pulseData = mutableListOf<Double>()
    private var recording = false;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainAddDataBinding.inflate(inflater, container, false)


        return binding.root

    }

    fun updatePulse() {
        var liveChartModel : AAChartModel = AAChartModel()
            .titleStyle(AAStyle.style("#FFFFFF"))
            .chartType(AAChartType.Area)
            .yAxisTitle("BPM")
            .axesTextColor("#FFFFFF")
            .title("Pulse")
            .backgroundColor("#4B1EC5")
            .series(arrayOf())

        Log.d("Threading", "Debug...")
        val pulseSeries = AASeriesElement()
            .color("#40B7FF")
            .name("Pulse")
            .data(pulseData.toTypedArray());

        binding.chartLiveInput.aa_refreshChartWithChartModel(liveChartModel.series(
            arrayOf(pulseSeries)))
    }

    fun startInfiniteExecution() {
        job = launch {
            while(true) {
                if(recording) {
                    Log.d("Recording", "Recording...")
                    pulseData.add(viewModel.getHeartBeat())
                    updatePulse()
                }
                delay(1_000)
            }
        }
    }

    fun cancelTask() {
        job?.cancel()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animationView: LottieAnimationView = binding.lavHeartBeat

        animationView.setAnimation("heart_beat_animation.json")

        val pulseSeries = AASeriesElement()
            .color("#40B7FF")
            .name("Pulse")
            .data(pulseData.toTypedArray());

        var liveChartModel : AAChartModel = AAChartModel()
            .titleStyle(AAStyle.style("#FFFFFF"))
            .chartType(AAChartType.Area)
            .yAxisTitle("BPM")
            .axesTextColor("#FFFFFF")
            .title("Pulse")
            .backgroundColor("#4B1EC5")
            .series(arrayOf(pulseSeries))

        binding.chartLiveInput.aa_drawChartWithChartModel(liveChartModel)
        var runJob = false;

        startInfiniteExecution();

        binding.lavHeartBeat.setOnTouchListener {_, event ->
            Log.d("Current Thread", "Start");
            Log.d("Current Thread", recording.toString());

            if (event.action == MotionEvent.ACTION_DOWN) {
                Log.d("Current Thread", "Down");
                recording = true;
                binding.lavHeartBeat.playAnimation();
            }
            if (event.action == MotionEvent.ACTION_UP) {
                Log.d("Current Thread", "UP");
                recording = false;
                binding.lavHeartBeat.pauseAnimation();
            }
            true
        }

        binding.fabRetry.setOnClickListener {
            pulseData = mutableListOf()
            updatePulse()
        }

        binding.btnSaveData.setOnClickListener {
            Log.d("SaveData", Date().toIsoString())

            val request = CreateCheckHealthRequest(
                pulseData.sum()/pulseData.size,
                0,
                Date(),
            )

            val authSharedPreferences = this.activity?.getSharedPreferences(
                AUTH_SHARED_PREFERENCES_NAME,
                AppCompatActivity.MODE_PRIVATE
            )

            var authKey = authSharedPreferences?.getString("authKey", "")

            Log.d("SaveData", request.toString())
            Log.d("SaveData", request.toJsonString())

            val httpAsync = "https://mazaya-health-check.deno.dev/create-check-data"
                .httpPost()
                .jsonBody(request.toJsonString())
                .authentication()
                .bearer(authKey.toString())
                .responseObject<CreateCheckHealthResponse> { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.d("ResponseResult", ex.toString())
                        }
                        is Result.Success -> {
                            val data = result.get()
                            Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            httpAsync.join()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}