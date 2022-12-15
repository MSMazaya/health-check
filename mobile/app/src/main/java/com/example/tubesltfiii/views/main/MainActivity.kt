package com.example.tubesltfiii.views.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.example.tubesltfiii.R
import com.example.tubesltfiii.databinding.ActivityMainBinding
import com.example.tubesltfiii.utils.BluetoothUtil
import com.example.tubesltfiii.views.main.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val addDataFragment = AddDataFragment()
        val graphsFragment = GraphsFragment()

        val address = intent.getStringExtra("DeviceAddress")
        val bluetoothUtil = BluetoothUtil(this)
        if (address != null) {
            Log.d("BLE-CONNECT", "Connecting...")
            Log.d("BLE-CONNECT", address)
            val bluetoothGatt = bluetoothUtil.connect(address, viewModel.bluetoothGattCallback)
            viewModel.setGatt(bluetoothGatt)
        }

        binding.bnvMain.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.miHome -> setCurrentFragment(homeFragment)
                R.id.miAddData -> setCurrentFragment(addDataFragment)
                R.id.miStats -> setCurrentFragment(graphsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_host_fragment_content_home, fragment)
            commit()
        }
    }
}