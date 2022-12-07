package com.example.tubesltfiii.views.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.tubesltfiii.R
import com.example.tubesltfiii.databinding.FragmentMainGraphsBinding
import com.example.tubesltfiii.databinding.FragmentMainHomeBinding
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class GraphsFragment : Fragment() {

    private var _binding: FragmentMainGraphsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentMainGraphsBinding.inflate(inflater, container, false)
        val hydrationChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .yAxisTitle("SpO2")
            .title("Oxygen Saturation")
            .backgroundColor("#FFFFFF")
            .series(arrayOf(
                AASeriesElement()
                    .color("#40B7FF")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
            )
            )

        val aaChartView = binding.chartHydrationLevel.aa_drawChartWithChartModel(hydrationChartModel)

        val pulseChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .yAxisTitle("BPM")
            .title("Pulse")
            .backgroundColor("#FFFFFF")
            .series(arrayOf(
                AASeriesElement()
                    .color("#40B7FF")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
            )
            )

        binding.chartPulse.aa_drawChartWithChartModel(pulseChartModel)
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