package com.example.tubesltfiii.utils.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice

class BluetoothScanCallback: BluetoothAdapter.LeScanCallback {
    private val devices: MutableList<BluetoothDevice> = mutableListOf()

    fun getDevices(): List<BluetoothDevice> {
        return devices.toList()
    }
    override fun onLeScan(device: BluetoothDevice?, rssi: Int, scanRecord: ByteArray?) {
        if (device != null) {
            devices.add(device)
        }
    }
}