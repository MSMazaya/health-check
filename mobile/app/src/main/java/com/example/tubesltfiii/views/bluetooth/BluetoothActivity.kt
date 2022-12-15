package com.example.tubesltfiii.views.bluetooth

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.tubesltfiii.databinding.ActivityBluetoothBinding
import com.example.tubesltfiii.views.bluetooth.viewmodel.BluetoothViewModel

class BluetoothActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBluetoothBinding
    private val viewModel: BluetoothViewModel by viewModels<BluetoothViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getDevices.observe(this, Observer { item ->
            item.forEach {
                Log.d("BluetoothDiscovery", it.name + " discovered")
            }
        })
    }
}