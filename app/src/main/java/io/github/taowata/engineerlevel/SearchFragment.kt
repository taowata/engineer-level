package io.github.taowata.engineerlevel

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import io.github.taowata.engineerlevel.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(this).get(SearchViewModel::class.java)
    }

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSearchBinding.inflate(inflater)

        // DataBindingにFragmentのライフサイクルでLiveDataのオブザーブを可能にする
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 円グラフの描画
        viewModel.languageAndBytes.observe(viewLifecycleOwner) {
            drawPieChart(it)
        }

        // レーダーチャートの描画
        viewModel.userProperties.observe(viewLifecycleOwner) {
            drawRadarChart(it)
        }

    }

    private fun drawPieChart(map: MutableMap<String, Long>) {

        //①Entryにデータ格納
        val entryList = mutableListOf<PieEntry>()
        val sortedMap = map.toList().sortedBy { it.second }.toMap()
        for((k, v) in sortedMap){
            entryList.add(
                PieEntry(v.toFloat(), k)
            )
        }

        //②PieDataSetにデータ格納
        val pieDataSet = PieDataSet(entryList, "candle")
        //③DataSetのフォーマット指定
        pieDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

        //④PieDataにPieDataSet格納
        val pieData = PieData(pieDataSet)
        //⑤PieChartにPieData格納
        val pieChart = binding.pieChart
        pieChart.data = pieData
        // 回転できなくする
        pieChart.isRotationEnabled = false
        //⑥Chartのフォーマット指定
        pieChart.legend.isEnabled = false
        //⑦PieChart更新
        pieChart.invalidate()
    }

    private fun drawRadarChart(triple: Triple<Int, Int, Float>) {
        //①Entryにデータ格納
        val entryList = mutableListOf<RadarEntry>()
        entryList.add(RadarEntry(triple.first.toFloat()))
        entryList.add(RadarEntry(triple.second.toFloat()))
        entryList.add(RadarEntry(triple.third))
        //②DataSetにデータ格納
        val radarDataSet = RadarDataSet(entryList, "エンジニア能力値")
        radarDataSet.apply {
            color = Color.rgb(103, 110, 129)
            fillColor = Color.rgb(103, 110, 129)
            setDrawFilled(true)
            fillAlpha = 180
            lineWidth = 2f
            isDrawHighlightCircleEnabled = true
        }

        val iRadarDataSet = mutableListOf<IRadarDataSet>()
        iRadarDataSet.add(radarDataSet)

        val labels = listOf("コミット数", "スター数", "FF比")

        val radarData = RadarData(iRadarDataSet)
        radarData.apply {
            setLabels(labels)
            setValueTextSize(8f)
            setDrawValues(false)
            setValueTextColor(Color.WHITE)
        }

        val chart = binding.radarChart
        val yAxis = chart.yAxis
        yAxis.setDrawLabels(false)
        yAxis.setLabelCount(5, false)
        chart.data = (radarData)
        chart.isRotationEnabled = false
        chart.description.isEnabled = false
        chart.legend.isEnabled = false
        chart.invalidate()

    }
}