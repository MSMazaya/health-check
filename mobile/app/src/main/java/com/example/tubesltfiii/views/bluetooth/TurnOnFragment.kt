package com.example.tubesltfiii.views.bluetooth

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.tubesltfiii.R
import com.example.tubesltfiii.databinding.FragmentBluetoothTurnOnBinding
import androidx.fragment.app.activityViewModels
import com.example.tubesltfiii.utils.BluetoothUtil
import com.example.tubesltfiii.views.bluetooth.viewmodel.BluetoothViewModel


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TurnOnFragment : Fragment() {

    private var _binding: FragmentBluetoothTurnOnBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: BluetoothViewModel by activityViewModels<BluetoothViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBluetoothTurnOnBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animationView: LottieAnimationView = binding.lavRadiation

        animationView.setAnimation("radiation_animation.json")

        binding.fabBluetooth.setOnClickListener {
            animationView.visibility = View.VISIBLE
            binding.tvConnecting.visibility = View.VISIBLE
            animationView.playAnimation()
            var counter = 0

            val bluetoothUtil = BluetoothUtil(requireContext())
            bluetoothUtil.scanLeDevice(viewModel.leScanCallback)

            val timer = object: CountDownTimer(10000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    if (counter == 3) {
                        counter = 0
                        binding.tvConnecting.text = "Searching"
                    }

                    counter++
                    val textUpdate = binding.tvConnecting.text.toString() + "."
                    binding.tvConnecting.text = textUpdate
                }
                override fun onFinish() {
                    findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                }
            }
            timer.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}