package io.github.taowata.engineerlevel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.taowata.engineerlevel.databinding.FragmentFavoriteListBinding

class FavoriteListFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteListBinding.inflate(inflater)

        val textView = binding.textView

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(firebaseUser?.uid ?: "unknownUser")
            .collection("engineers")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val text = textView.text.toString() + document.data["name"]
                    textView.text = text
                }
            }

        return binding.root
    }

}