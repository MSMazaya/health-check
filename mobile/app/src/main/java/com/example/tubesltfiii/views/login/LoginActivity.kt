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
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.jackson.responseObject
import com.github.kittinunf.result.Result;
import dev.bluefalcon.*

var REQUEST_ENABLE_BT = 0;

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.editTextTextPersonName.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            val authSharedPreferences =
                getSharedPreferences(AUTH_SHARED_PREFERENCES_NAME, MODE_PRIVATE)
            val editor = authSharedPreferences.edit()

            val request = LoginRequest(
                email,
                password
            )

            Log.d("ResponseResult", request.toString())
            Log.d("ResponseResult", request.toJsonString())
            Log.d(
                "Bluetooth",
                ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                    .toString()
            )
            Log.d("Bluetooth", PackageManager.PERMISSION_GRANTED.toString())
//            Log.d("Bluetooth", ContextCompat.checkSelfPermission(this, "android.permission.BLUETOOTH_SCAN").toString())

            binding.textView13.text = "Should start tho"
            try {
//                val delegate = BLEDelegate();
//                val blueFalcon = BlueFalcon(this.application, null)
//                blueFalcon.delegates.add(delegate)
//                val something = blueFalcon.scan()
    // THIS HERE IS THE DOCS BRO https://developer.android.com/guide/topics/connectivity/bluetooth/setup
                val bluetoothManager: BluetoothManager = getSystemService(BluetoothManager::class.java)
                val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.getAdapter()
                val permission = arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
                ActivityCompat.requestPermissions(this, permission, 0)
                if (bluetoothAdapter != null) {
                    if (!bluetoothAdapter.isEnabled
                        &&
                        PackageManager.PERMISSION_GRANTED == ActivityCompat
                            .checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN)
                    ) {
                        Log.d("BLE-TEST", "cancelled")
//                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
                        val permission = arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
                        ActivityCompat.requestPermissions(this, permission, 0)
                    } else {
                        Log.d("BLE-TEST", "allowed")
                        var finalText = ""
                        val pairedDevices: Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
                        pairedDevices?.forEach { device ->
                            val deviceName = device.name
                            val deviceHardwareAddress = device.address // MAC address
                            finalText += deviceName + " @ " + deviceHardwareAddress + ", "
                        }

                        Log.d("BLE-TEST", finalText)
                    }
                }

//
//
//                Log.d("Bluetooth", something.toString())
//                Log.d("Bluetooth", "Pemission Suffice")
//
//                val timer = object: CountDownTimer(10000, 1000) {
//                    override fun onTick(millisUntilFinished: Long) {
//                        val devices = delegate.getDevices()
//                        binding.textView13.text = "In it"
//                        var finalText = "Wait... " + devices.size.toString()
//                        for (device in devices) {
//                            finalText += device.name + ", "
//                        }
//                        binding.textView13.text = finalText
//                    }
//                    override fun onFinish() {
//                        // do something
//                    }
//                }
//                timer.start()
//
//            } catch(error: BluetoothPermissionException) {
//                binding.textView13.text = "Error"
//                Log.d("Bluetooth", "Permission Error")
//                Log.d("Bluetooth", "Permission Error: ${error.toString()}")
//
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
//                    val permission = arrayOf(Manifest.permission.BLUETOOTH_SCAN)
//                    ActivityCompat.requestPermissions(this, permission, 0)
//                }
//            }

//            val httpAsync = "https://mazaya-health-check.deno.dev/login"
//                .httpPost()
//                .jsonBody(request.toJsonString())
//                .responseObject<LoginResponse> { request, response, result ->
//                    when (result) {
//                        is Result.Failure -> {
//                            val ex = result.getException()
//                            Log.d("ResponseResult", ex.toString())
//                        }
//                        is Result.Success -> {
//                            val data = result.get()
//
//                            if (data.status == 200 && data.payload?.authKey != null) {
//                                editor.apply {
//                                    putString("authKey", data.payload?.authKey)
//                                    commit()
//                                }
//
//                                Intent(this, BluetoothActivity::class.java).also {
//                                    startActivity(it)
//                                }
//
//                                Log.d("ResponseResult", data.payload?.authKey.toString())
//                            } else {
//                                Toast.makeText(this, data.message, Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//
//            httpAsync.join()
            } catch (error: Error) {
                Log.d("Error", error.toString())
            }
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

class BLEDelegate : BlueFalconDelegate {
    private var devices: MutableList<BluetoothPeripheral> = mutableListOf()

    fun discoveredDevices(bles: List<BluetoothPeripheral>) {
        devices = bles.toMutableList()
    }

    fun getDevices(): MutableList<BluetoothPeripheral> {
        return devices
    }

    override fun didCharacteristcValueChanged(
        bluetoothPeripheral: BluetoothPeripheral,
        bluetoothCharacteristic: BluetoothCharacteristic
    ) {
        TODO("Not yet implemented")
    }

    override fun didConnect(bluetoothPeripheral: BluetoothPeripheral) {
        TODO("Not yet implemented")
    }

    override fun didDisconnect(bluetoothPeripheral: BluetoothPeripheral) {
        TODO("Not yet implemented")
    }

    override fun didDiscoverCharacteristics(bluetoothPeripheral: BluetoothPeripheral) {
        TODO("Not yet implemented")
    }

    override fun didDiscoverDevice(
        bluetoothPeripheral: BluetoothPeripheral,
        advertisementData: Map<AdvertisementDataRetrievalKeys, Any>
    ) {
        Log.d("BLUETOOTH", bluetoothPeripheral.name.toString())
        Log.d("BLUETOOTH", bluetoothPeripheral.name.toString())
        Log.d("BLUETOOTH", bluetoothPeripheral.uuid)
        Log.d("BLUETOOTH", bluetoothPeripheral.services.size.toString())
        bluetoothPeripheral.services.forEach {
            Log.d("BLE-SERVICE",it.name.toString())
            it.characteristics.forEach {
                Log.d("BLE-SERVICE-CHARACTERISTIC",it.name.toString())
                Log.d("BLE-SERVICE-CHARACTERISTIC",it.value.toString())
            }
        }
    }

    override fun didDiscoverServices(bluetoothPeripheral: BluetoothPeripheral) {

    }

    override fun didReadDescriptor(
        bluetoothPeripheral: BluetoothPeripheral,
        bluetoothCharacteristicDescriptor: BluetoothCharacteristicDescriptor
    ) {
        TODO("Not yet implemented")
    }

    override fun didRssiUpdate(bluetoothPeripheral: BluetoothPeripheral) {
        TODO("Not yet implemented")
    }

    override fun didUpdateMTU(bluetoothPeripheral: BluetoothPeripheral) {
        TODO("Not yet implemented")
    }

    override fun didWriteCharacteristic(
        bluetoothPeripheral: BluetoothPeripheral,
        bluetoothCharacteristic: BluetoothCharacteristic,
        success: Boolean
    ) {
        TODO("Not yet implemented")
    }
}