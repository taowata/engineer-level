package io.github.taowata.engineerlevel.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://api.github.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GitHubApiService {
    @GET("users/{userName}")
    suspend fun getUser(@Path("userName") userName: String): GitHubUser

    @GET("users/{userName}/repos")
    suspend fun getRepositories(@Path("userName") userName: String): List<Repository>

    // 特定レポジトリの1年間のコミット数を取得する
    @GET("repos/{userName}/{repoName}/stats/participation")
    suspend fun getCommitList(
        @Path("userName") userName: String,
        @Path("repoName") repoName: String): CommitList
}

object GitHubApi {
    val retrofitService : GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java)
    }
}