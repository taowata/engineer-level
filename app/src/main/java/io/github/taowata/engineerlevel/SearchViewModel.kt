package io.github.taowata.engineerlevel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taowata.engineerlevel.network.CommitList
import io.github.taowata.engineerlevel.network.GitHubApi
import io.github.taowata.engineerlevel.network.Repository
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    // viewModel初期化時に通信
//    init {
//        getGitHubUserProperties()
//    }

    private fun getGitHubUserProperties() {
        viewModelScope.launch {
            try {
                val userName = "taowata"
                val gitHubUser = GitHubApi.retrofitService.getUser(userName)
                val repositories = GitHubApi.retrofitService.getRepositories(userName)

                // コミット数の取得
                val allCommitNumber = calculateTotalCommits(userName, repositories)

                _response.value = "Success: @${gitHubUser.userName} has ${repositories.size} repositories and $allCommitNumber Commits"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

    private suspend fun calculateTotalCommits(userName: String, repositories: List<Repository>): Int {
        var allCommitNumber = 0

        for (repository in repositories) {
            val repoName = repository.name
            val commitList: CommitList = GitHubApi.retrofitService.getCommitList(userName, repoName)
            allCommitNumber += commitList.commitList.sum()
        }

        return allCommitNumber
    }

}