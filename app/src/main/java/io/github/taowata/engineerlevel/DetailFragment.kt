package io.github.taowata.engineerlevel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import io.github.taowata.engineerlevel.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val safeArgs: DetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater)

        // 受け取ったEngineerオブジェクトをBind
        val engineer = safeArgs.engineer
        binding.engineer = engineer

        // 棒グラフの描画
        Drawer.drawBarChart(binding.barChart, engineer.languageAndBytes)

        return binding.root
    }
}