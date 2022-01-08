package com.acdetorres.app.dashboard.fragments.adapters

import android.content.Context
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.acdetorres.app.MainActivity
import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.acdetorres.app.databinding.RvDashboardBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DashboardAdapter(
    val size : Int,
    var termResponse : GetSearchTermResponse?,
    val listener : DashboardRvInterface
) : RecyclerView.Adapter<DashboardAdapter.VHDashboard>() {

    lateinit var mContext : Context

    inner class VHDashboard(val binding : RvDashboardBinding) : RecyclerView.ViewHolder (binding.root) {
        fun bind (response : GetSearchTermResponse?) {

            val timer = object : CountDownTimer(500, 500) {
                override fun onTick(p0: Long) {
                    //Nothing
                }

                override fun onFinish() {
                    val term = binding.etSearchBox.text.toString()
                    if (term.isNotEmpty()) {
                        listener.onSearch(term)
                    }

                }
            }

            //Cancels and starts the polling after text change of searching of term
            binding.etSearchBox.doAfterTextChanged {
                timer.cancel()
                timer.start()
            }

            binding.rvTracks.layoutManager = LinearLayoutManager(mContext)

            if (response != null) {
                val adapter = ItemTracksAdapter(response.results, object : ItemTracksAdapter.ItemTracksAdapterInterface {
                    override fun onClick(track: GetSearchTermResponse.Result) {
                        //TODO
                    }
                })

                binding.rvTracks.adapter = adapter

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHDashboard {
        val binding = RvDashboardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mContext = parent.context
        return VHDashboard(binding)
    }

    override fun onBindViewHolder(holder: VHDashboard, position: Int) {
        holder.bind(termResponse)
    }

    override fun getItemCount(): Int {
        return size
    }

    interface DashboardRvInterface {
        fun onSearch(term : String)
    }
}