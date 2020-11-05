package io.github.taowata.engineerlevel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taowata.engineerlevel.network.GitHubApi
import kotlinx.coroutines.launch
import java.lang.Exception

class SearchViewModel : ViewModel() {

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
                val gitHubUser = GitHubApi.retrofitService.getUser()
                val repositories = GitHubApi.retrofitService.getRepositories()
                _response.value = "Success: @${gitHubUser.userName} has ${repositories.size} repositories"
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }

}