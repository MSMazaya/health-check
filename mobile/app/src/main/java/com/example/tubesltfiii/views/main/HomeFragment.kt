package com.example.tubesltfiii.views.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.tubesltfiii.R
import com.example.tubesltfiii.databinding.FragmentMainHomeBinding
import com.example.tubesltfiii.datas.CheckHistory
import com.example.tubesltfiii.datas.Device
import com.example.tubesltfiii.datas.constants.AUTH_SHARED_PREFERENCES_NAME
import com.example.tubesltfiii.datas.http.check_health.CheckHealthResponse
import com.example.tubesltfiii.datas.http.login.LoginRequest
import com.example.tubesltfiii.datas.http.login.LoginResponse
import com.example.tubesltfiii.views.bluetooth.BluetoothActivity
import com.example.tubesltfiii.views.bluetooth.CheckHistoryCardAdapter
import com.example.tubesltfiii.views.bluetooth.DeviceCardAdapter
import com.github.kittinunf.fuel.core.extensions.authentication
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.Result
import java.util.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentMainHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)

        val authSharedPreferences = this.activity?.getSharedPreferences(
            AUTH_SHARED_PREFERENCES_NAME,
            AppCompatActivity.MODE_PRIVATE
        )

        var authKey = authSharedPreferences?.getString("authKey", "")

        val httpAsync = "https://mazaya-health-check.deno.dev/get-check-data"
            .httpPost()
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

                        val checksTransformed = data.payload?.map {
                            CheckHistory(
                                "Oxygen Saturation: " + it.oxygenSaturation.toString() + " SpO2",
                                "Pulse: " + it.pulse.toString() + " BPM",
                                it.date.slice(IntRange(0,9)) + " " + it.date.slice(IntRange(11,15))
                            )
                        }

                        if(activity != null) {
                            if (checksTransformed !== null) {
                                binding.rvCheckHistories.adapter = CheckHistoryCardAdapter(checksTransformed)
                            }
                        }
                    }
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