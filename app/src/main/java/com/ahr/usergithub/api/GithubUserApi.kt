package com.ahr.usergithub.api

import com.ahr.usergithub.BuildConfig
import com.ahr.usergithub.model.ResponseSearchUser
import com.ahr.usergithub.model.User
import com.ahr.usergithub.model.UserDetail
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface GithubUserApi {

    companion object {
        private const val BASE_URL = "https://api.github.com"
        private const val token = BuildConfig.GITHUB_TOKEN

        val instance: GithubUserApi by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(GithubUserApi::class.java)
        }
    }

    @Headers(
        "Authorization: $token",
        "User-Agent: request"
    )
    @GET("users")
    fun getUser(): Call<ArrayList<User>>

    @Headers(
        "Authorization: $token",
        "User-Agent: request"
    )
    @GET("search/users")
    fun getUserByUsername(
        @Query("q") username: String
    ): Call<ResponseSearchUser>

    @Headers(
        "Authorization: $token",
        "User-Agent: request"
    )
    @GET("users/{username}")
    fun getUser(@Path("username") username: String): Call<UserDetail>

    @Headers(
        "Authorization: $token",
        "User-Agent: request"
    )
    @GET
    fun getUserFollow(@Url url: String): Call<ArrayList<User>>
}