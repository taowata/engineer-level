package io.github.taowata.engineerlevel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taowata.engineerlevel.network.CommitList
import io.github.taowata.engineerlevel.network.GitHubApi
import io.github.taowata.engineerlevel.network.Repository
import io.github.taowata.engineerlevel.network.StarGazer
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel : ViewModel() {

    private val _commitNumber = MutableLiveData<Int>()
    private val commitNumber: LiveData<Int>
        get() = _commitNumber

    private val _starNumber = MutableLiveData<Int>()
    private val starNumber: LiveData<Int>
        get() = _starNumber

    private val _ffRatio = MutableLiveData<Double>()
    private val ffRatio: LiveData<Double>
        get() = _ffRatio

    val userProperties: LiveData<Triple<Int, Int, Double>>
        get(): LiveData<Triple<Int, Int, Double>> {
            val triple: Triple<Int, Int, Double> = Triple(commitNumber.value ?: 0, starNumber.value ?: 0, ffRatio.value ?: 0.0)
            val mutableLiveData = MutableLiveData<Triple<Int, Int, Double>>()
            mutableLiveData.value = triple
            return mutableLiveData
        }

    private val _languageAndBytes = MutableLiveData<MutableMap<String, Long>>()
    val languageAndBytes: LiveData<MutableMap<String, Long>>
        get() = _languageAndBytes

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    // viewModel初期化時に通信
    init {
        getGitHubUserProperties()
    }

    private fun getGitHubUserProperties() {
        viewModelScope.launch {
            try {
                val userName = "Ossamoon"
                val gitHubUser = GitHubApi.retrofitService.getUser(userName)
                val repositories = GitHubApi.retrofitService.getRepositories(userName)

                // Triple<Int, Int, MutableMap<String, Long>が返される
                val (allCommitNumber, allStarNumber, languageMap) = calculateUserProperties(userName, repositories)

                _response.value =
                    "Success: @${gitHubUser.userName} has ${repositories.size} repositories and $allCommitNumber Commits, $allStarNumber Stars." +
                            "@${gitHubUser.userName}'s languages: $languageMap"

                _commitNumber.value = allCommitNumber
                _starNumber.value = allStarNumber
                _languageAndBytes.value = languageMap
                _ffRatio.value = gitHubUser.followers.toDouble() / gitHubUser.following

            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

    private suspend fun calculateUserProperties(
        userName: String,
        repositories: List<Repository>): Triple<Int, Int, MutableMap<String, Long>> {
        var allCommitNumber = 0
        var allStarNumber = 0
        val languageMap: MutableMap<String, Long> = mutableMapOf()
        for (repository in repositories) {
            val repoName = repository.name

            // コミット数の計算
            val commitList: CommitList = GitHubApi.retrofitService.getCommitList(userName, repoName)
            allCommitNumber += commitList.commitList.sum()

            // スター数の計算
            val starGazerList: List<StarGazer> =
                GitHubApi.retrofitService.getStarGazerList(userName, repoName)
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

        return Triple(allCommitNumber, allStarNumber, languageMap)
    }
}