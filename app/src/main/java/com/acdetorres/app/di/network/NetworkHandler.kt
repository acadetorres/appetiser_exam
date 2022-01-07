package com.acdetorres.app.di.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*

//This class would handle all the errors from any API calls.
class NetworkHandler{

    //Catch the error, onStart emits the Loading State(true), onCompletion emits the Loading state(false)
    fun <T:Any>handleRequests(response : Flow<Resource<T>>, isBackground : Boolean = false) : Flow<Resource<T>> {

        return response.catch { exception ->
            //Here we can catch each exception like http exception, malformed responses, etc.  For the simplicity for now we'll just emit the message from exception
            emit(Resource.Loading(false))
            emit(Resource.Error(exception.message.toString()))

        }.onStart {
            emit(Resource.Loading(true))
//            delay(1000) // This is a bug, on consecutive emit, it bugs and not emitting the next values so this is added
        }.onCompletion {
            //For post value and background api calls that needs not be drawn or have small data to draw on screen
            if (isBackground) {
                delay(100)// This is a bug, on consecutive emit, it bugs and not emitting the next values so this is added
            }
            emit(Resource.Loading(false))
        }
    }
}