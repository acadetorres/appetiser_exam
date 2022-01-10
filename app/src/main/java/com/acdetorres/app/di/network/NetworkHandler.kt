package com.acdetorres.app.di.network

import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber

//This class would handle all the errors from any API calls.
class NetworkHandler{

    //Catch the error, onStart emits the Loading State
    fun <T:Any>handleRequests(response : Flow<Resource<T>>, isMainThread : Boolean = false) : Flow<Resource<T>> {

        return response.catch { exception ->
            //Here we can catch each exception like http exception, malformed responses, etc.  For the simplicity for now we'll just emit the message from exception
            emit(Resource.Loading(false))

            Timber.e("exception : ${exception.message.toString()}")
            emit(Resource.Error(exception.message.toString()))
        }.onStart {
            emit(Resource.Loading(true))
        }
    }
}