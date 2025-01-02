package com.example.lec29

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsCallable {
    @GET("/v2/top-headlines?apiKey=c44e5457822e48d1be84a589584821af")
    fun getNews(
        @Query("category") cat: String,
        @Query("country") code:String)
    : Call<News>
}