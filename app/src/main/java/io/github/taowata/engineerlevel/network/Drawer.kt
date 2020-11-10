package io.github.taowata.engineerlevel.network

import android.graphics.Color
import android.util.Log
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.github.mikephil.charting.utils.ColorTemplate

object Drawer {

    fun drawBarChart(barChart: BarChart, map: MutableMap<String, Long>) {
        // Entryの作成
        val entryList = mutableListOf<BarEntry>()
        val sortedMap = map.toList().sortedByDescending { it.second }.toMap()
        var sortedLabelList = sortedMap.keys.toMutableList()
        sortedLabelList.add(0, "") //先頭に原点用の空白を入れる
        sortedLabelList = sortedLabelList.subList(0, 5)
        Log.i("barchart", sortedLabelList.toString())

        val yValues = sortedMap.values.toMutableList().subList(0, 4)
        Log.i("barchart", yValues.toString())
        for(i in yValues.indices){
            entryList.add(
                BarEntry((i + 1).toFloat(), yValues[i].toFloat())
            )
        }

        val barDataSet = BarDataSet(entryList, "square")
        val colorList = listOf(Color.GREEN, Color.YELLOW, Color.BLUE, Color.CYAN)
        barDataSet.colors = colorList


        val barData = BarData(barDataSet)

        barChart.data = barData


        val yAxisRight = barChart.axisRight
        yAxisRight.apply {
            setDrawLabels(false)
            setDrawGridLines(false)
            setDrawZeroLine(true)
            setDrawTopYLabelEntry(true)
        }

        val xAxis = barChart.xAxis
        xAxis.apply {
            valueFormatter = IndexAxisValueFormatter(sortedLabelList)
            labelCount = sortedLabelList.size - 1
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            setDrawGridLines(false)
            setDrawAxisLine(true)
            isEnabled = true
            textColor = Color.BLACK
        }

        barChart.description.isEnabled = false
        barChart.legend.isEnabled = false
        barChart.invalidate()
    }

    fun drawPieChart(pieChart: PieChart, map: MutableMap<String, Long>) {

        //①Entryにデータ格納
        val entryList = mutableListOf<PieEntry>()
        val sortedMap = map.toList().sortedByDescending { it.second }.toMap()
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
        pieChart.data = pieData
        // 回転できなくする
        pieChart.isRotationEnabled = false
        //⑥Chartのフォーマット指定
        pieChart.legend.isEnabled = false
        //⑦PieChart更新
        pieChart.invalidate()
    }

    fun drawRadarChart(radarChart: RadarChart, triple: Triple<Int, Int, Float>) {
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

        val yAxis = radarChart.yAxis
        yAxis.setDrawLabels(false)
        yAxis.setLabelCount(5, false)

        radarChart.apply {
            data = (radarData)
            isRotationEnabled = false
            description.isEnabled = false
            legend.isEnabled = false
            invalidate()
        }

    }

}