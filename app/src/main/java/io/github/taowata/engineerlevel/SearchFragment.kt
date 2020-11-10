package io.github.taowata.engineerlevel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import io.github.taowata.engineerlevel.databinding.FragmentSearchBinding
import io.github.taowata.engineerlevel.network.Drawer

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
            Drawer.drawBarChart(binding.barChart, it)
        }

    }

}