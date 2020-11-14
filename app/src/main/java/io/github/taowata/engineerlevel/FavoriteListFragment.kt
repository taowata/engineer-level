package io.github.taowata.engineerlevel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.taowata.engineerlevel.data.Engineer
import io.github.taowata.engineerlevel.databinding.FragmentFavoriteListBinding

class FavoriteListFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteListBinding.inflate(inflater)

        val viewModel: FavoriteListViewModel by viewModels()

        // RecyclerViewのセットアップ
        val recyclerView = binding.engineerList
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        val itemClickAction: (Engineer) -> Unit = {
            val view = requireView()
            view.findNavController().navigate(R.id.action_favoriteListFragment_to_detailFragment)
        }
        val adapter = FavoriteEngineerAdapter(itemClickAction)
        recyclerView.adapter = adapter

        viewModel.engineerList.observe(viewLifecycleOwner) {
            adapter.setEngineers(it)
            adapter.notifyDataSetChanged()
        }


        return binding.root
    }

}