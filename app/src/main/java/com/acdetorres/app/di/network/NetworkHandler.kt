package com.acdetorres.app.di.network

import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import java.lang.Exception

//This class would handle all the errors from any API calls.
class NetworkHandler{

    fun <T:Any>handleRequests(response : T) = flow {
        emit(Resource.Loading(true))

        // THIS is a bug and needs to be added on consecutive data emission
        delay(100)

        try {
            emit(Resource.Success(response))
        } catch (e : Exception) {
            emit(Resource.Error(e.message.toString()))
        }

        emit(Resource.Loading(false))
    }
}