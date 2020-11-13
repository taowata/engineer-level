package io.github.taowata.engineerlevel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore
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
            Drawer.drawBarChart(binding.barChart, it)
        }

        binding.logoutButton.setOnClickListener {
            AuthUI.getInstance()
                .signOut(requireContext())
                .addOnCompleteListener {
                    view.findNavController().navigate(R.id.action_searchFragment_to_authFragment)
                }

        }

        binding.floatingActionButton.setOnClickListener {
            val db = FirebaseFirestore.getInstance()
            val engineer = hashMapOf(
                "name" to "taowata",
                "contributions" to 923,
                "followers" to 2,
                "stars" to 1,
                "languagesAndBytes" to hashMapOf(
                    "JavaScript" to 2972783,
                    "Kotlin" to 671871,
                    "HTML" to 222565,
                    "CSS" to 27053,
                )
            )
            db.collection("engineers").document(engineer["name"].toString())
                .set(engineer)
                .addOnSuccessListener { documentReference ->
                    Log.d("tag", "DocumentSnapshot added with ID: $documentReference")
                }
                .addOnFailureListener { e ->
                    Log.w("tag", "Error adding document", e)
                }
        }

    }

}