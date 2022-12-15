package com.example.tubesltfiii.utils

import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.content.Context
import androidx.core.content.ContextCompat

class BluetoothUtil {
    private lateinit var bluetoothAdapter: BluetoothAdapter;
    private lateinit var bluetoothLeScanner: BluetoothLeScanner;
    private var bluetoothGatt: BluetoothGatt? = null;
    private var context: Context;
    private var scanning = false
    private val SCAN_PERIOD: Long = 10000


    constructor(context_: Context) {
        context = context_
        val bluetoothManager: BluetoothManager? =
            ContextCompat.getSystemService(context_, BluetoothManager::class.java)
        if (bluetoothManager != null) {
            bluetoothAdapter = bluetoothManager.getAdapter()
            bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        }
    }

    fun scanLeDevice(leScanCallback: ScanCallback) {
        val handler = android.os.Handler()
        if (!scanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                scanning = false
                bluetoothLeScanner.stopScan(leScanCallback)
            }, SCAN_PERIOD)
            scanning = true
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            scanning = false
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    fun connect(address: String, bluetoothGattCallback: BluetoothGattCallback): BluetoothGatt {
        val device = bluetoothAdapter.getRemoteDevice(address)

        return device.connectGatt(context, true, bluetoothGattCallback)
    }
}