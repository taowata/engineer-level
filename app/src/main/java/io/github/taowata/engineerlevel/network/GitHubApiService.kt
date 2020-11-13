package io.github.taowata.engineerlevel.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.taowata.engineerlevel.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
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
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    suspend fun getUser(@Path("userName") userName: String): GitHubUser

    @GET("users/{userName}/repos?per_page=100")
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    suspend fun getRepositories(@Path("userName") userName: String): List<Repository>

    // 特定レポジトリの1年間のコミット数を取得する
    @GET("repos/{userName}/{repoName}/stats/participation")
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    suspend fun getCommitList(
        @Path("userName") userName: String,
        @Path("repoName") repoName: String,
    ): CommitList

    // 特定リポジトリのスター数(スターしたユーザー数)を取得する
    @GET("repos/{userName}/{repoName}/stargazers?per_page=100")
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    suspend fun getStarGazerList(
        @Path("userName") userName: String,
        @Path("repoName") repoName: String,
    ): List<StarGazer>

    // 特定リポジトリの言語ごとのByte数を取得
    @GET("repos/{userName}/{repoName}/languages")
    @Headers("Authorization: token ${BuildConfig.ACCESS_TOKEN}")
    suspend fun getLanguageBytes(
        @Path("userName") userName: String,
        @Path("repoName") repoName: String,
    ): MutableMap<String, Long>
}

object GitHubApi {
    val retrofitService : GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java)
    }
}