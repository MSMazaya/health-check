package com.example.tubesltfiii.views.bluetooth.viewmodel

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BluetoothViewModel : ViewModel()  {
    private val devices = MutableLiveData<MutableSet<BluetoothDevice>>()

    val getDevices: LiveData<MutableSet<BluetoothDevice>> get() = devices

    private fun addDevice(device: BluetoothDevice) {
        Log.d("BLE-SCAN", device.name + " is discovered")
        if(device?.name != null) {
            val newMutableList = mutableSetOf<BluetoothDevice>(device)
            devices.value?.forEach {
                newMutableList.add(it)
            }
            devices.value = newMutableList
        }
        Log.d("BLE-SCAN", devices.value.toString())
    }

    val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            addDevice(result.device)
        }
    }
}