package com.acdetorres.app.di.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.lang.Exception

//This class would handle all the errors from any API calls.
class NetworkHandler{

    fun <T:Any>handleRequests(response : T) = flow {
        emit(Resource.Loading(true))

        // THIS is a bug and needs to be added on consecutive data emission
        delay(100)

        try {
            emit(Resource.Loading(false))
            emit(Resource.Success(response))
        } catch (e : Exception) {
            //Here we can emit OR transform the data for a data class we need for exposing non 200 status or other exceptions
            Timber.e("ERROR MESSAGE : ${e.message.toString()}")
            emit(Resource.Loading(false))
            emit(Resource.Error(e.message.toString()))
        }

    }
}