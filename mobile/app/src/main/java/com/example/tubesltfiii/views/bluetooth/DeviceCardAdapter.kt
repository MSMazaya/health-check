package com.example.tubesltfiii.views.bluetooth

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubesltfiii.R
import com.example.tubesltfiii.datas.Device
import com.example.tubesltfiii.views.main.MainActivity
import com.google.android.material.card.MaterialCardView

class DeviceCardAdapter(
    private val context: Context,
    private val devices: List<Device>,
) : RecyclerView.Adapter<DeviceCardAdapter.DeviceCardViewHolder>() {

    class DeviceCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tvDeviceName)
        val card: MaterialCardView = itemView.findViewById(R.id.mcvDeviceCard)
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

        holder.card.setOnClickListener {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun getItemCount(): Int {
        return devices.size
    }
}