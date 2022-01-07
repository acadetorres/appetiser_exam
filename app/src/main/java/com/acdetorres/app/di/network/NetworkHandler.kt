package com.acdetorres.app.di.network

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
import java.lang.Exception

//This class would handle all the errors from any API calls.
class NetworkHandler{

    fun <T:Any>handleRequests(response : Flow<Resource<T>>) : Flow<Resource<T>> {

        return response.catch { exception ->
            //Here we can catch each exception like http exception, malformed responses, etc.  For the simplicity for now we'll just emit the message from exception
            emit(Resource.Loading(false))
            emit(Resource.Error(exception.message.toString()))

        }.onStart {
            emit(Resource.Loading(true))
            delay(100) // This is a bug, on consecutive emit, it bugs and not emitting the next values so this is added
        }
    }
}