package com.acdetorres.app.dashboard.fragments.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.acdetorres.app.dashboard.repository.SearchRepository
import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.acdetorres.app.di.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val repo : SearchRepository
) : ViewModel() {

    val searchResult : LiveData<Resource<GetSearchTermResponse>> get() = _searchResult

    val _searchResult = MutableLiveData<Resource<GetSearchTermResponse>>()

    suspend fun getSearchResult(term : String)  {
        repo.getSearchedTerm(term).collect {
            _searchResult.postValue(it)
        }
    }

}