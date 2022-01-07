package com.acdetorres.app.dashboard.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.acdetorres.app.R
import com.acdetorres.app.dashboard.fragments.viewmodel.DashboardViewModel
import com.acdetorres.app.databinding.FragmentDashboardBinding
import com.acdetorres.app.di.network.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FragmentDashboard : Fragment() {

    val viewModel by viewModels<DashboardViewModel>()

    //View binding
    lateinit var binding : FragmentDashboardBinding

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



        lifecycleScope.launch {
            viewModel.getSearchResult("star")
        }



        viewModel.searchResult.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> {

                    Timber.e(it.toString())
                    Snackbar.make(binding.root, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    Timber.e(it.toString())
                    showLoading(it.loading)
                }
                is Resource.Success -> {

                    if (it.data != null) {
                        val data = it.data
                        Timber.e("Result[0] ${data.results[0].artistName}")
                    }
                }
            }
        })
    }

    fun showLoading(isLoading : Boolean) {

    }

}