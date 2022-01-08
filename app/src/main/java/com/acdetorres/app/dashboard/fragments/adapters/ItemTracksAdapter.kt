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

    lateinit var mContext : Context

    inner class viewHolder(val binding : ItemTracksBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind (track : GetSearchTermResponse.Result, position : Int) {

            Timber.e("Check if onbind is called : $position")

            binding.tvTrackname.text = track.trackName

            binding.tvGenre.text = track.primaryGenreName

            binding.tvPrice.text = track.trackPrice.toString()

            binding.tvThumbnail.setBackgroundColor(getMatColor("500"))

            binding.tvThumbnail.text = track.trackName[0].toString()
            
            Glide.with(binding.root)
                .load(track.artworkUrl60)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(binding.ivArtwork)

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
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface ItemTracksAdapterInterface {
        fun onClick(track : GetSearchTermResponse.Result)
    }

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