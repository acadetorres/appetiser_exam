package com.acdetorres.app.di.network

//This exposes network state to Fragments/Activity
sealed class Resource<out T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading(val loading : Boolean) : Resource<Nothing>()
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}