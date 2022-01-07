package com.acdetorres.app.dashboard.repository

import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.acdetorres.app.di.network.ApiService
import com.acdetorres.app.di.network.NetworkHandler
import com.acdetorres.app.di.network.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject


//Repository, all data will be fetched and stored here.
class SearchRepository @Inject constructor(val apiService: ApiService) {

    private val networkHandler = NetworkHandler()

    suspend fun getSearchedTerm(term : String) : Flow<Resource<out GetSearchTermResponse>> {
        return networkHandler.handleRequests(apiService.getSearchTerm(term, "au", "movie"))
    }
}