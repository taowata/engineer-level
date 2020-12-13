package io.github.taowata.engineerlevel

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.taowata.engineerlevel.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SearchViewModel : ViewModel() {

    private val _gitHubUser = MutableLiveData<GitHubUser>()
    val gitHubUser: LiveData<GitHubUser> = _gitHubUser

    private val _contributions = MutableLiveData<Int>()
    val contributions: LiveData<Int> = _contributions

    private val _followers = MutableLiveData<Int>()
    val followers: LiveData<Int> = _followers

    private val _stars = MutableLiveData<Int>()
    val stars: LiveData<Int> = _stars

    private val _languageAndBytes = MutableLiveData<MutableMap<String, Long>>()
    val languageAndBytes: LiveData<MutableMap<String, Long>>
        get() = _languageAndBytes

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage = _errorMessage

    private val hasSearchFinished: MutableLiveData<Boolean>  = MutableLiveData(false)
    private val isSearching: MutableLiveData<Boolean> = MutableLiveData(false)

    val resultVisibility: LiveData<Int> = hasSearchFinished.map { hasSearchFinished ->
        if (hasSearchFinished) View.VISIBLE else View.GONE
    }
    val progressBarVisibility: LiveData<Int> = isSearching.map { isSearching ->
        if (isSearching) View.VISIBLE else View.GONE
    }


    fun getGitHubUserProperties(userName: String) {

        isSearching.value = true
        hasSearchFinished.value = false

        viewModelScope.launch {
            try {
                val gitHubUser = GitHubApi.retrofitService.getUser(userName)
                _gitHubUser.value = gitHubUser
                val repositories = GitHubApi.retrofitService.getRepositories(userName)

                // コントリビューション数のスクレイピング
                _contributions.value =  withContext(Dispatchers.IO) {
                    return@withContext GitHubHomeHtmlParser.getContributionNumber("https://github.com/$userName")
                }


                // フォロワー数の取得
                _followers.value = gitHubUser.followers

                // スター数、使用言語を計算
                val (allStars, languageMap) = calculateUserProperties(userName, repositories)

                _stars.value = allStars
                _languageAndBytes.value = languageMap

                // viewの反映
                isSearching.value = false
                hasSearchFinished.value = true

            } catch (e: Exception) {
                _errorMessage.value = "Failure: ${e.message}"
            }
        }
    }


    private suspend fun calculateUserProperties(
        userName: String,
        repositories: List<Repository>): Pair<Int, MutableMap<String, Long>> {

        var allStarNumber = 0
        val languageMap: MutableMap<String, Long> = mutableMapOf()

        for (repository in repositories) {
            val repoName = repository.name

            // スター数の計算
            val starGazerList: List<StarGazer> =
                GitHubApi.retrofitService.getStarGazerList(userName, repoName)
            Log.i("tag", starGazerList.toString())
            allStarNumber += starGazerList.size

            // 使用言語のByte数の計算
            val tempMap: MutableMap<String, Long> =
                GitHubApi.retrofitService.getLanguageBytes(userName, repoName)

            for ((k, v) in tempMap) {
                if (languageMap[k] == null) {
                    languageMap += k to v
                } else {
                    val pastBytes: Long = languageMap[k] ?: 0L
                    val addBytes: Long = tempMap[k] ?: 0L
                    languageMap[k] = pastBytes + addBytes
                }
            }

        }

        return Pair(allStarNumber, languageMap)
    }

    fun addGitHubUserToFireStore() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        val engineer = hashMapOf(
            "name" to gitHubUser.value?.userName,
            "contributions" to contributions.value,
            "followers" to followers.value,
            "stars" to stars.value,
            "languagesAndBytes" to languageAndBytes.value,
            "avatar_url" to gitHubUser.value?.avatar_url,
        )

        db.collection("users").document(firebaseUser?.uid ?: "unknownUser")
            .collection("engineers").document(engineer["name"].toString())
            .set(engineer)
            .addOnSuccessListener {
                Log.d("tag", "DocumentSnapshot added with ID: ${firebaseUser?.uid}")
            }
            .addOnFailureListener { e ->
                Log.w("tag", "Error adding document", e)
            }
    }

}