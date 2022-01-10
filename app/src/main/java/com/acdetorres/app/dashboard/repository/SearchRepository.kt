package com.acdetorres.app.dashboard.repository

import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.acdetorres.app.di.network.ApiService
import com.acdetorres.app.di.network.NetworkHandler
import com.acdetorres.app.di.network.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

import javax.inject.Inject


//Repository, all data will be fetched and stored here.
class SearchRepository @Inject constructor(private val apiService: ApiService) {

    //Custom flow handler to catch errors so no try catch block are repeated
    private val networkHandler = NetworkHandler()

    //Returns a Flow that can collected of type Resource<GetSearchTermResponse>
    suspend fun getSearchedTerm(term : String) : Flow<Resource<GetSearchTermResponse>> {
        val flow = flow {
            val response = apiService.getSearchTerm(term, "au", "movie")
            //emits loading false before emitting success and data since the onStart of the flow is handled in networkHandler
            emit(Resource.Loading(false))
            emit(Resource.Success(response))
        }
        return networkHandler.handleRequests(flow)
    }
}