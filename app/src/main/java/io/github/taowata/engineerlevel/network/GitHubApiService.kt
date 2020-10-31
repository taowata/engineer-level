package io.github.taowata.engineerlevel.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.github.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface GitHubApiService {
    @GET("users/taowata")
    fun getProperties():
            Call<String>
}

object GitHubApi {
    val retrofitService : GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java) }
}