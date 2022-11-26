package com.example.tubesltfiii.views.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tubesltfiii.R
import com.example.tubesltfiii.databinding.FragmentMainHomeBinding
import com.example.tubesltfiii.datas.CheckHistory
import com.example.tubesltfiii.datas.Device
import com.example.tubesltfiii.views.bluetooth.CheckHistoryCardAdapter
import com.example.tubesltfiii.views.bluetooth.DeviceCardAdapter

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentMainHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainHomeBinding.inflate(inflater, container, false)

        val data = listOf<CheckHistory>(
            CheckHistory("Hydration Level: 90%", "Pulse: 100 bpm", "12/11/2022 17:58"),
            CheckHistory("Hydration Level: 90%", "Pulse: 100 bpm", "12/11/2022 17:58"),
            CheckHistory("Hydration Level: 90%", "Pulse: 100 bpm", "12/11/2022 17:58"),
            CheckHistory("Hydration Level: 90%", "Pulse: 100 bpm", "12/11/2022 17:58"),
        )

        if(activity != null) {
            binding.rvCheckHistories.adapter = CheckHistoryCardAdapter(data)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}