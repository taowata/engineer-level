package io.github.taowata.engineerlevel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.taowata.engineerlevel.network.GitHubApi
import io.github.taowata.engineerlevel.network.GitHubUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    // viewModel初期化時に通信
    init {
        getGitHubUserProperties()
    }

    private fun getGitHubUserProperties() {
        GitHubApi.retrofitService.getUser().enqueue(
            object: Callback<GitHubUser> {
                override fun onResponse(call: Call<GitHubUser>, response: Response<GitHubUser>) {
                    _response.value = "Success: We got @${response.body()?.userName}'s information "
                }

                override fun onFailure(call: Call<GitHubUser>, t: Throwable) {
                    _response.value = "Failure: " + t.message
                }
            }
        )
    }

}