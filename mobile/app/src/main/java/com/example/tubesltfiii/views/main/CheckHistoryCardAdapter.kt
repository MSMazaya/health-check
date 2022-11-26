package com.example.tubesltfiii.views.bluetooth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tubesltfiii.R
import com.example.tubesltfiii.datas.CheckHistory

class CheckHistoryCardAdapter(
    private val data: List<CheckHistory>,
) : RecyclerView.Adapter<CheckHistoryCardAdapter.CheckHistoryCardViewHolder>() {

    class CheckHistoryCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hydrationLevelTextView: TextView = itemView.findViewById(R.id.tvHydrationLevel)
        val pulseTextView: TextView = itemView.findViewById(R.id.tvPulse)
        val datetimeTextView: TextView = itemView.findViewById(R.id.tvDatetime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckHistoryCardViewHolder {
        return CheckHistoryCardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.check_history_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CheckHistoryCardViewHolder, position: Int) {
        val curData = data[position]
        holder.hydrationLevelTextView.text = curData.hydrationLevel
        holder.pulseTextView.text = curData.pulse
        holder.datetimeTextView.text = curData.date
    }

    override fun getItemCount(): Int {
        return data.size
    }
}