package io.github.taowata.engineerlevel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.taowata.engineerlevel.data.Engineer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteListViewModel : ViewModel() {

    private val _engineerList: MutableLiveData<MutableList<Engineer>> =
        MutableLiveData(mutableListOf())
    val engineerList: LiveData<MutableList<Engineer>> = _engineerList

    init {
        init()
    }

    private fun init() {
        viewModelScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            val db = FirebaseFirestore.getInstance()
            val list = mutableListOf<Engineer>()

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
                            languageAndBytes = languagesAndByte as HashMap<String, Long>,
                            avatar_url = data["avatar_url"].toString(),
                        )
                        list.add(engineer)
                    }
                    _engineerList.value = list
                }
                .addOnFailureListener { exception ->
                    Log.d("tag", "Error getting documents: ", exception)
                }

        }
    }

}