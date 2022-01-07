package com.acdetorres.app.di.network

import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    //Base Url is https://itunes.apple.com/
    //This is https://itunes.apple.com/search with Query parameters
    @GET("search")
    suspend fun getSearchTerm(
        @Query("term") term : String,
        @Query("country") country : String,
        @Query("media") media : String
    ) : GetSearchTermResponse

}