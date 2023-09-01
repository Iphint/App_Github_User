package com.arifin.myapplication.data.retrofit

import com.arifin.myapplication.data.response.DetailUserResponse
import com.arifin.myapplication.data.response.GithubResponse
import com.arifin.myapplication.data.response.ResponseFollowersItem
import com.arifin.myapplication.data.response.ResponseFollowingItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("search/users")
    fun searchUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(@Path("username") username: String): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<ResponseFollowersItem>>

    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<ResponseFollowingItem>>
}