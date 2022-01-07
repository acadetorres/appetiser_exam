package com.acdetorres.app.dashboard.fragments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acdetorres.app.dashboard.repository.SearchRepository
import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.acdetorres.app.di.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val repo : SearchRepository
) : ViewModel() {

    //Init screen, randomize for every restart or new startup of app

    init {
        //Launched in Main since UI Draw is priority and postValue on live data needs the delay fix that disrupts the user experience
        viewModelScope.launch(Dispatchers.Main) {
            getSearchResult(getRandomString(1))
        }
    }

    //Random String generator
    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    //Observable to UI, this handles the persistence of the state of the app
    val searchResult : LiveData<Resource<GetSearchTermResponse>> get() = _searchResult

    private val _searchResult = MutableLiveData<Resource<GetSearchTermResponse>>()

    //executes and collects the search and result of API call then updates _searchResult
    //so UI can observe the changes
    suspend fun getSearchResult(term : String)  {
        repo.getSearchedTerm(term).collect {
            _searchResult.value = it
        }
    }

}