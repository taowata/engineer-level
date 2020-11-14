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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val engineer = safeArgs.engineer

        binding.textView.text = engineer.name
    }
}