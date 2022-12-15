package com.example.tubesltfiii.views.login

import AdvertisementDataRetrievalKeys
import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.tubesltfiii.databinding.ActivityLoginBinding
import com.example.tubesltfiii.datas.constants.AUTH_SHARED_PREFERENCES_NAME
import com.example.tubesltfiii.datas.http.login.LoginRequest
import com.example.tubesltfiii.datas.http.login.LoginResponse
import com.example.tubesltfiii.views.bluetooth.BluetoothActivity
import com.example.tubesltfiii.views.main.MainActivity
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.Result;

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val authSharedPreferences =
            getSharedPreferences(AUTH_SHARED_PREFERENCES_NAME, MODE_PRIVATE)

//        if(authSharedPreferences.getString("authKey", "") != "") {
//            Intent(this, BluetoothActivity::class.java).also {
//                startActivity(it)
//            }
//        }

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextTextPersonName.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            val editor = authSharedPreferences.edit()

            val request = LoginRequest(
                email,
                password
            )

            val httpAsync = "https://mazaya-health-check.deno.dev/login"
                .httpPost()
                .jsonBody(request.toJsonString())
                .responseObject<LoginResponse> { request, response, result ->
                    when (result) {
                        is Result.Failure -> {
                            val ex = result.getException()
                            Log.d("ResponseResult", ex.toString())
                        }
                        is Result.Success -> {
                            val data = result.get()

                            if (data.status == 200 && data.payload?.authKey != null) {
                                editor.apply {
                                    putString("authKey", data.payload?.authKey)
                                    commit()
                                }

                                Intent(this, BluetoothActivity::class.java).also {
                                    startActivity(it)
                                }

                                Log.d("ResponseResult", data.payload?.authKey.toString())
                            } else {
                                Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            httpAsync.join()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                Log.d("PermissionRequest", "${permissions[i]} granted.")
            }
        }
    }
}