package com.acdetorres.app.dashboard.fragments.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.acdetorres.app.dashboard.repository.api_response.GetSearchTermResponse
import com.acdetorres.app.databinding.ItemTracksBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import timber.log.Timber

class ItemTracksAdapter(
    val data : List<GetSearchTermResponse.Result>,
    val onClickInterface : ItemTracksAdapterInterface

): RecyclerView.Adapter<ItemTracksAdapter.viewHolder>() {

    //Context used for random material color generator
    lateinit var mContext : Context

    inner class viewHolder(val binding : ItemTracksBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (track : GetSearchTermResponse.Result) {


            //Displays texts and Glides load the image to the imageview

            binding.tvTrackname.text = track.trackName

            binding.tvGenre.text = track.primaryGenreName

            binding.tvPrice.text = track.trackPrice.toString()

            binding.tvThumbnail.setBackgroundColor(getMatColor("500"))

            binding.tvThumbnail.text = track.trackName[0].toString()
            
            Glide.with(binding.root)
                .load(track.artworkUrl100)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //Cache for faster loading
                .into(binding.ivArtwork)

            //When the layout is clicked means the item is clicked
            binding.clLayout.setOnClickListener {
                onClickInterface.onClick(track)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        mContext = parent.context
        val binding = ItemTracksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        holder.bind(data[position])
        Timber.e("Position of onBindViewHolder : $position")
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemTracksAdapterInterface {
        //interface to FragmentDashboard
        fun onClick(track : GetSearchTermResponse.Result)
    }


    //Random Material Color Generator
    private fun getMatColor(typeColor: String): Int {
        var returnColor: Int = Color.BLACK
        val arrayId = mContext.resources.getIdentifier(
            "mdcolor_$typeColor",
            "array",
            mContext.packageName
        )
        if (arrayId != 0) {
            val colors = mContext.resources.obtainTypedArray(arrayId)
            val index = (Math.random() * colors.length()).toInt()
            returnColor = colors.getColor(index, Color.BLACK)
            colors.recycle()
        }
        return returnColor
    }
}