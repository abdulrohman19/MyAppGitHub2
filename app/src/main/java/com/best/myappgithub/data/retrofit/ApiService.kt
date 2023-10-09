package com.best.myappgithub.data.retrofit

import com.best.myappgithub.data.response.DetailUserResponse
import com.best.myappgithub.data.response.GithubResponse
import com.best.myappgithub.data.response.ItemsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getUsers(
        @Query("q") username: String
    ) : Call<GithubResponse>

    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<ItemsItem>>

    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<ItemsItem>>
}