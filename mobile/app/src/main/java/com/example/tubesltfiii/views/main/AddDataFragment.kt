package com.example.tubesltfiii.views.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.tubesltfiii.R
import com.example.tubesltfiii.databinding.FragmentMainAddDataBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStyle

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AddDataFragment : Fragment() {

    private var _binding: FragmentMainAddDataBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainAddDataBinding.inflate(inflater, container, false)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animationView: LottieAnimationView = binding.lavHeartBeat

        animationView.setAnimation("heart_beat_animation.json")
        animationView.playAnimation()

        val liveChartModel : AAChartModel = AAChartModel()
            .titleStyle(AAStyle.style("#FFFFFF"))
            .chartType(AAChartType.Area)
            .yAxisTitle("BPM")
            .axesTextColor("#FFFFFF")
            .title("Pulse")
            .backgroundColor("#4B1EC5")
            .series(arrayOf(
                AASeriesElement()
                    .color("#40B7FF")
                    .name("Pulse")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                    .name("Hydration Level")
                    .data(arrayOf(7.0, 0, 12, 15, 18.2, 10.5, 10.2, 10.5, 14.3, 18.3, 20.9, 10.6)),
            )
            )

        binding.chartLiveInput.aa_drawChartWithChartModel(liveChartModel)
//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}