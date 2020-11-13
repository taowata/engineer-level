package io.github.taowata.engineerlevel

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.taowata.engineerlevel.data.Engineer
import io.github.taowata.engineerlevel.databinding.FragmentFavoriteListBinding
import kotlin.reflect.typeOf

class FavoriteListFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFavoriteListBinding.inflate(inflater)

        // RecyclerViewのセットアップ
        val recyclerView = binding.engineerList
        val itemClickAction: (Engineer) -> Unit = {
            // 画面遷移実装 TODO()
        }
        val adapter = FavoriteEngineerAdapter(itemClickAction)
        recyclerView.adapter = adapter

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val engineerList = mutableListOf<Engineer>()

        db.collection("users").document(firebaseUser?.uid ?: "unknownUser")
            .collection("engineers")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val data = document.data
                    val languagesAndByte = data["languagesAndBytes"]

                    val engineer = Engineer(
                        name = data["name"].toString(),
                        contributions = data["contributions"].toString().toInt(),
                        followers = data["followers"].toString().toInt(),
                        stars = data["stars"].toString().toInt(),
                        languageAndBytes = languagesAndByte as HashMap<String, Long>
                    )
                    Log.i("tag", engineer.languageAndBytes.toString())
                    engineerList.add(engineer)
                }
            }

        adapter.setEngineers(engineerList)
        adapter.notifyDataSetChanged()

        return binding.root
    }

}