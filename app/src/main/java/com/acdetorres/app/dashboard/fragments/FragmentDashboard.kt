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
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.acdetorres.app.MainActivity
import com.acdetorres.app.dashboard.fragments.adapters.ItemTracksAdapter
import com.acdetorres.app.dashboard.fragments.viewmodel.DashboardViewModel
import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.acdetorres.app.databinding.FragmentDashboardBinding
import com.acdetorres.app.di.network.Resource
import com.acdetorres.app.di.shared_pref.SharedPref
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentDashboard : Fragment() {

    val viewModel by viewModels<DashboardViewModel>()

    //View binding
    lateinit var binding : FragmentDashboardBinding

    //Injects shared pref, this should be on the repository! But I got terrible flu and no longer have time today.
    @Inject
    lateinit var sharedPref : SharedPref

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

        //Checks if lastViewedTrack is not null, then navigate to it
        val lastViewedTrack = sharedPref.getLastSelectedTrack()

        if (lastViewedTrack != null) {
            FragmentDashboardDirections.actionFragmentDashboardToFragmentSelectedTrack(
                lastViewedTrack.trackName,
                lastViewedTrack.previewUrl,
                lastViewedTrack.genre,
                lastViewedTrack.price,
                lastViewedTrack.description,
                lastViewedTrack.wrapperType,
                lastViewedTrack.trackUrl
            ).run {
                findNavController().navigate(this)
            }
        }

        //Observes the liveData
        observeLiveData()

        //setup the recyclerview's layout manager
        binding.rvTracks.layoutManager = LinearLayoutManager(requireContext())

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

                        //Adapter of the Recycler View
                        val adapter = ItemTracksAdapter(data.results, object : ItemTracksAdapter.ItemTracksAdapterInterface {
                            override fun onClick(track: GetSearchTermResponse.Result) {

                                sharedPref.storeSelectedTrackDetails(track)

                                FragmentDashboardDirections.actionFragmentDashboardToFragmentSelectedTrack(
                                    if (track.trackName.isNullOrEmpty()) "" else track.trackName,
                                    if (track.previewUrl.isNullOrEmpty()) "" else track.previewUrl,
                                    track.primaryGenreName,
                                    track.trackPrice.toString(),
                                    track.longDescription,
                                    track.wrapperType,
                                    track.trackViewUrl
                                ).run {
                                    findNavController().navigate(this)
                                }

                            }
                        })

                        binding.rvTracks.adapter = adapter
                    }
                }
            }
        })
    }

    //Show Loading state or not
    fun showLoading(isLoading : Boolean) {
        (activity as MainActivity).showLoading(isLoading)
    }

    override fun onResume() {
        super.onResume()
        val timer = object : CountDownTimer(500, 500) {
            override fun onTick(p0: Long) {
                //Nothing
            }

            override fun onFinish() {
                //Launched in Main thread since I'm using setValue, because postValue would sometimes discards the previous values before the last
                lifecycleScope.launch(Dispatchers.Main) {
                    //Current value of the SearchBox
                    val term = binding.etSearchBox.text.toString()
                    if (term.isNotEmpty()) {
                        //Closes the keyboard and gets the Search Result
                        (activity as MainActivity).closeKeyboard()
                        viewModel.getSearchResult(term)
                    }
                }
            }
        }

        //Cancels and starts the polling after text change of searching of term
        binding.etSearchBox.doAfterTextChanged {
            timer.cancel()
            timer.start()
        }
    }

}