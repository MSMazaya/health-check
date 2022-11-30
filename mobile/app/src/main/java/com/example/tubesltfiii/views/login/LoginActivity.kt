package com.example.tubesltfiii.views.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tubesltfiii.databinding.ActivityLoginBinding
import com.example.tubesltfiii.datas.constants.AUTH_SHARED_PREFERENCES_NAME
import com.example.tubesltfiii.datas.http.login.LoginRequest
import com.example.tubesltfiii.datas.http.login.LoginResponse
import com.example.tubesltfiii.views.bluetooth.BluetoothActivity
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

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextTextPersonName.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            val authSharedPreferences = getSharedPreferences(AUTH_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
            val editor = authSharedPreferences.edit()

            val request = LoginRequest(
                email,
                password
            )

            Log.d("ResponseResult", request.toString())
            Log.d("ResponseResult", request.toJsonString())

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

                            editor.apply {
                                putString("authKey", data.payload.authKey)
                            }

                            Intent(this, BluetoothActivity::class.java).also {
                                startActivity(it)
                            }

                            Log.d("ResponseResult", data.payload.authKey)
                        }
                    }
                }

            httpAsync.join()
//            Intent(this, BluetoothActivity::class.java).also {
//                startActivity(it)
//            }
        }
    }
}