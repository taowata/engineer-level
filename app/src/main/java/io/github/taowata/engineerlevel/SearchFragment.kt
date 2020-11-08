package io.github.taowata.engineerlevel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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

        viewModel.languageAndBytes.observe(viewLifecycleOwner) {
            drawPieChart(it)
        }

    }

    private fun drawPieChart(map: MutableMap<String, Long>) {

        //①Entryにデータ格納
        val entryList = mutableListOf<PieEntry>()
        for((k, v) in map){
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
        //⑥Chartのフォーマット指定
        pieChart.legend.isEnabled = false
        //⑦PieChart更新
        pieChart.invalidate()
    }
}