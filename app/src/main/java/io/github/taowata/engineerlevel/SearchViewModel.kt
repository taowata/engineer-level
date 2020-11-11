package io.github.taowata.engineerlevel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taowata.engineerlevel.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SearchViewModel : ViewModel() {

    // コミット数、スター数、フォロワー数
    private val _userProperties = MutableLiveData<Triple<Int, Int, Int>>()
    val userProperties: LiveData<Triple<Int, Int, Int>>
        get() = _userProperties

    private val _languageAndBytes = MutableLiveData<MutableMap<String, Long>>()
    val languageAndBytes: LiveData<MutableMap<String, Long>>
        get() = _languageAndBytes

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage = _errorMessage

    private val _gitHubUser = MutableLiveData<GitHubUser>()
    val gitHubUser: LiveData<GitHubUser> = _gitHubUser

    private val _contributions = MutableLiveData<String>()
    val contributions: LiveData<String> = _contributions

    // viewModel初期化時に通信
    init {
        getGitHubUserProperties()
    }

    private fun getGitHubUserProperties() {
        viewModelScope.launch {
            try {
                val userName = "taowata"
                val gitHubUser = GitHubApi.retrofitService.getUser(userName)
                _gitHubUser.value = gitHubUser
                val repositories = GitHubApi.retrofitService.getRepositories(userName)

                _contributions.value =  withContext(Dispatchers.IO) {
                    return@withContext GitHubHomeHtmlParser.getContributionNumber("https://github.com/taowata")
                }
                    // Triple<Int, Int, MutableMap<String, Long>が返される
                    val (allCommitNumber, allStarNumber, languageMap) = calculateUserProperties(userName, repositories)

                    // 0で割らないための対策
                    val followers = gitHubUser.followers

                    val triple: Triple<Int, Int, Int> = Triple(allCommitNumber, allStarNumber, followers)
                    _userProperties.value = triple

                    _languageAndBytes.value = languageMap

            } catch (e: Exception) {
                _errorMessage.value = "Failure: ${e.message}"
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