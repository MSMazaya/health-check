package com.example.tubesltfiii.views.bluetooth

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.tubesltfiii.R
import com.example.tubesltfiii.databinding.FragmentBluetoothSearchResultBinding
import com.example.tubesltfiii.datas.Device
import com.example.tubesltfiii.views.bluetooth.viewmodel.BluetoothViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class ResultsFragment : Fragment() {

    private var _binding: FragmentBluetoothSearchResultBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: BluetoothViewModel by activityViewModels<BluetoothViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBluetoothSearchResultBinding.inflate(inflater, container, false)

        val data = viewModel.getDevices.value?.toList()

        Log.d("BLE-RESULT", data.toString())

        if(activity != null && data != null) {
            binding.rvDevices.adapter = DeviceCardAdapter(requireActivity(), data)
        }

        binding.fabRetry.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
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