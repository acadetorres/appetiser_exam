package com.acdetorres.app.di.network

import kotlinx.coroutines.cancel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import timber.log.Timber

//This class would handle all the errors from any API calls.
class NetworkHandler{

    //Catch the error, onStart emits the Loading State, onCompletion emits the Loading and Success Data
    fun <T:Any>handleRequests(response : Flow<Resource<T>>, isMainThread : Boolean = false) : Flow<Resource<T>> {

        return response.catch { exception ->
            //Here we can catch each exception like http exception, malformed responses, etc.  For the simplicity for now we'll just emit the message from exception
            emit(Resource.Loading(false))

            if (!isMainThread) {
                delay(100)
            }
            emit(Resource.Error(exception.message.toString()))
            currentCoroutineContext().cancel(null)
        }.onStart {
            emit(Resource.Loading(true))
        }.onCompletion {
            //The delay here is a bug on postValue, not sure if it was thread blocked but for some reason delay(100) works got it from stackoverflow though.
            //You may of course use .value but I prefer to do network cals in the background thread
            //isBackground thread an optional condition if there are need for the network call to be the background thread set this to true
            //response.collect is so we can expose the network state to false without making Resource.Success null.
            if (isMainThread) {
                response.collect {
                    if (it is  Resource.Success) {
                        emit(Resource.Loading(false))
                        emit(it)
                    }
                }
            } else {
                delay(100)
                response.collect {
                    if (it is Resource.Success) {
                        delay(200)
                        emit(Resource.Loading(false))
                        delay(100)
                        emit(it)
                    }
                }
            }
        }
    }
}