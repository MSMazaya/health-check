package com.example.tubesltfiii.views.main.viewmodel

import android.bluetooth.*
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel() {
    private val gattServices = MutableLiveData<List<BluetoothGattService>>()
    private var gatt : BluetoothGatt? = null
    var connected = false;

    val getGattServices get() = gattServices.value

    fun setGatt(gatt_: BluetoothGatt) {
        Log.d("BLE-CONNECT", "Gatt SET")
        gatt = gatt_
    }

    val bluetoothGattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server
                connected = true
                gatt?.discoverServices()
                Log.d("BLE-CONNECT", "Connected to device")
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                connected = false
                Log.d("BLE-CONNECT", "Disconnected from device")
                // disconnected from the GATT Server
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            val service = gatt?.getService(UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b"))
            val characteristic = service?.getCharacteristic(UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8"))
            val characteristic2 = service?.getCharacteristic(UUID.fromString("60467bc8-f5e3-4fca-8f86-ce2d6183d7c2"))

            if(characteristic != null) {
                gatt.readCharacteristic(characteristic)
                gatt.readCharacteristic(characteristic2)
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int
        ) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d("BLE-CHARACTERISTIC", "Connected")
            }
        }
    }

    fun getHeartBeat(): Double {
        val service = gatt?.getService(UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b"))
        val characteristic = service?.getCharacteristic(UUID.fromString("60467bc8-f5e3-4fca-8f86-ce2d6183d7c2"))
        gatt?.readCharacteristic(characteristic)
        val pulse = characteristic?.getStringValue( 0);
        Log.d("HeartBeat", pulse.toString())
        if (pulse != null) {
            return pulse.toDouble()
        }

        return 0.0
    }

    fun getSPO2(): Double {
        val service = gatt?.getService(UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b"))
        val characteristic = service?.getCharacteristic(UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8"))
        gatt?.readCharacteristic(characteristic)
        val spo2 = characteristic?.getStringValue( 0);
        Log.d("SPO2", spo2.toString())
        if (spo2 != null) {
            return spo2.toDouble()
        }

        return 0.0
    }
}