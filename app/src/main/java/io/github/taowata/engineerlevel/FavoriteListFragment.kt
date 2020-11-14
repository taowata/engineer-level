package io.github.taowata.engineerlevel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
            val action = FavoriteListFragmentDirections.actionFavoriteListFragmentToDetailFragment(it)
            view.findNavController().navigate(action)
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