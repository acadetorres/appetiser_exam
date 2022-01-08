package com.acdetorres.app.dashboard.fragments

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.acdetorres.app.MainActivity
import com.acdetorres.app.dashboard.fragments.adapters.DashboardAdapter
import com.acdetorres.app.dashboard.fragments.adapters.ItemTracksAdapter
import com.acdetorres.app.dashboard.fragments.viewmodel.DashboardViewModel
import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.acdetorres.app.databinding.FragmentDashboardBinding
import com.acdetorres.app.di.network.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.notify

@AndroidEntryPoint
class FragmentDashboard : Fragment() {

    val viewModel by viewModels<DashboardViewModel>()

    //View binding
    lateinit var binding : FragmentDashboardBinding

    private lateinit var adapter : DashboardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeLiveData()

        binding.rvDashboard.layoutManager = LinearLayoutManager(requireContext())

        adapter = DashboardAdapter(1, null, object : DashboardAdapter.DashboardRvInterface {
            override fun onSearch(term: String) {
                lifecycleScope.launch {
                    viewModel.getSearchResult(term)
                }

            }

        })

        binding.rvDashboard.adapter = adapter

    }


    //Observes the livedata being updated with calls from user interaction
    fun observeLiveData() {
        //LiveData handles it's lifecycle!
        viewModel.searchResult.observe(viewLifecycleOwner, {
            when (it) {
                //Displays the errors
                is Resource.Error ->  Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT).show()

                //Displays the loading state
                is Resource.Loading -> showLoading(it.loading)

                //Displays the result from the API calls
                is Resource.Success -> {
                    if (it.data != null) {
                        val data = it.data

                        adapter.termResponse = data

                        adapter.notifyDataSetChanged()


                    }
                }
            }
        })
    }

    fun showLoading(isLoading : Boolean) {
        (activity as MainActivity).showLoading(isLoading)
    }

}