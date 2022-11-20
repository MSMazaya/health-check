package com.example.tubesltfiii.views.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tubesltfiii.databinding.ActivityLoginBinding
import com.example.tubesltfiii.views.bluetooth.BluetoothActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            Intent(this, BluetoothActivity::class.java).also {
                startActivity(it)
            }
        }
    }
}