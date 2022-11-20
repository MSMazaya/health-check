package com.example.tubesltfiii.views.bluetooth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tubesltfiii.databinding.FragmentSecondBinding
import com.example.tubesltfiii.datas.Device

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        val data = listOf<Device>(
            Device("Example Device: AAAA-AAAA-AAAA-AAAA"),
            Device("Example Device: AAAA-AAAA-AAAA-AAAA"),
            Device("Example Device: AAAA-AAAA-AAAA-AAAA"),
            Device("Example Device: AAAA-AAAA-AAAA-AAAA"),
            Device("Example Device: AAAA-AAAA-AAAA-AAAA"),
        )
        Log.d("data", data.size.toString())
        binding.rvDevices.adapter = DeviceCardAdapter(data)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}