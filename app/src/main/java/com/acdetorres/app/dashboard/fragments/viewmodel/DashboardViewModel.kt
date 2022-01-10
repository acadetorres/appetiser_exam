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

    init {
        //Launched in main since postValue sometimes discards the previous values before the last
        viewModelScope.launch(Dispatchers.Main) {
            //Randomize search result
            getSearchResult(getRandomString(1))
        }
    }

    //Random String generator
    private fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    //Observable to UI, this handles the persistence of the state of the app even on configuration changes
    val searchResult : LiveData<Resource<GetSearchTermResponse>> get() = _searchResult

    private val _searchResult = MutableLiveData<Resource<GetSearchTermResponse>>()

    //executes and collects the search result of API call then updates _searchResult
    //so UI can observe the changes
    suspend fun getSearchResult(term : String)  {
        repo.getSearchedTerm(term).collect {
            _searchResult.value = it
        }
    }

}