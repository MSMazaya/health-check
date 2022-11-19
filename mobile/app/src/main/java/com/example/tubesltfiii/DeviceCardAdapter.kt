package com.example.tubesltfiii

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubesltfiii.databinding.ActivityBluetoothBinding
import com.example.tubesltfiii.databinding.DeviceCardBinding

class DeviceCardAdapter(
    private val devices: List<Device>,
) : RecyclerView.Adapter<DeviceCardAdapter.DeviceCardViewHolder>() {

    class DeviceCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvDeviceName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceCardViewHolder {
        return DeviceCardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.device_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DeviceCardViewHolder, position: Int) {
        val curDevice = devices[position]
        holder.textView.text = curDevice.name
        Log.d("Bindview", "in bind view")
    }

    override fun getItemCount(): Int {
        Log.d("GetItemCount", devices.size.toString())
        return devices.size
    }
}